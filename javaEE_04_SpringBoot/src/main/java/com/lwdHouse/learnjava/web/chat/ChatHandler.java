package com.lwdHouse.learnjava.web.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwdHouse.learnjava.entity.User;
import com.lwdHouse.learnjava.service.redis.RedisService;
import com.lwdHouse.learnjava.web.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ChatHandler extends TextWebSocketHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ChatHistory chatHistory;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RedisService redisService;

    // 保存所有Client的WebSocket会话实例:
    private Map<String, WebSocketSession> clients = new ConcurrentHashMap<>();

    public void broadCastMessages(ChatMessage chat) throws IOException {
        TextMessage message = toTextMessage(List.of(chat));
        for (String id : clients.keySet()) {
            WebSocketSession session = clients.get(id);
            session.sendMessage(message);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String s = message.getPayload().strip();
        if (s.isEmpty()){
            return;
        }
        String name = (String) session.getAttributes().get("name");
        ChatText chat = objectMapper.readValue(s, ChatText.class);
        ChatMessage msg = new ChatMessage(name, chat.text);
        chatHistory.addToHistory(msg);
        broadCastMessages(msg);
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        clients.put(session.getId(), session);
        String name = null;
        Long id = (Long) session.getAttributes().get(UserController.KEY_USER_ID);
        if (id != null){
            String json = redisService.hget(UserController.KEY_USER_ID, id.toString());
            if (json != null){
                User user = objectMapper.readValue(json, User.class);
                if (user != null){
                    name = user.getName();
                }
            }
        }
        if (name == null){
            name = initGuestName();
        }
        session.getAttributes().put("name", name);
        logger.info("webSocket Connection established: id = {}, name = {}", session.getId(), name);
        // 把历史消息发给新用户
        List<ChatMessage> list = chatHistory.getHistory();
        session.sendMessage(toTextMessage(list));
        // 添加系统消息并广播
        var msg = new ChatMessage("SYSTEM MESSAGE", name + " joined the room.");
        chatHistory.addToHistory(msg);
        broadCastMessages(msg);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        clients.remove(session.getId());
        logger.info("webSocket connection closed: id = {}, close-status = {}", session.getId(), status);
    }

    private TextMessage toTextMessage(List<ChatMessage> messages) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(messages);
        return new TextMessage(json);
    }

    private String initGuestName(){
        return "Guest" + this.guestNumber.incrementAndGet();
    }

    // 原子自增器，保证线程安全
    private AtomicInteger guestNumber = new AtomicInteger();
}
