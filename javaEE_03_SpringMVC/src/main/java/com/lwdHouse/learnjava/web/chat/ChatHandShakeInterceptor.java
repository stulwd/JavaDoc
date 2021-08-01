package com.lwdHouse.learnjava.web.chat;

import com.lwdHouse.learnjava.web.UserController;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.List;

@Component
public class ChatHandShakeInterceptor extends HttpSessionHandshakeInterceptor {
    public ChatHandShakeInterceptor() {
        // 主要作用是在WebSocket建立连接后，把HttpSession的一些属性复制到WebSocketSession，例如，用户的登录信息等
        super(List.of(UserController.KEY_USER));
    }
}
