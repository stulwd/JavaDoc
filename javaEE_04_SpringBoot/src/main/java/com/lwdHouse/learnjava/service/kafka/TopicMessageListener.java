package com.lwdHouse.learnjava.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwdHouse.learnjava.service.rabbitMQ.messaging.LoginMessage;
import com.lwdHouse.learnjava.service.rabbitMQ.messaging.RegistrationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * kafka consumer
 */

@Component
public class TopicMessageListener {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ObjectMapper objectMapper;

    /*topics指定该方法监听的topic名*/
    /*假如producer发送的消息是A、B、C、D、
      Group ID相同的多个Consumer实际上被视作一个Consumer，他们各自接受A、B、C、D某一部分（即同一个消息只能被一个Consumer接受）
      Group ID不同表示这是两个不同的consumer，它们将分别收取完整的消息流A、B、C、D*/
    @KafkaListener(topics = "topic_registration", groupId = "group1")
    /*@Payload表示传入的是消息正文*/
    /*@Header可传入消息的指定Header*/
    public void onRegistrationMessage(@Payload String message, @Header("type") String type) throws JsonProcessingException {
        // 根据Class名来反序列化message获得MessageBean
        RegistrationMessage msg = objectMapper.readValue(message, getType(type));
        logger.info("received registration message: {}", msg);
    }

    @KafkaListener(topics = "topic_login", groupId = "group1")
    public void onLoginMessage(@Payload String message, @Header("type") String type) throws JsonProcessingException {
        LoginMessage msg = objectMapper.readValue(message, getType(type));
        logger.info("received login message: {}", msg);
    }

    @KafkaListener(topics = "topic_login", groupId = "group2")
    public void processLoginMessage(@Payload String message, @Header("type") String type) throws JsonProcessingException {
        LoginMessage msg = objectMapper.readValue(message, getType(type));
        logger.info("process login message: {}", msg);
    }

    private static <T> Class<T> getType(String type){
        // TODO: use cache
        try {
            return (Class<T>) Class.forName(type);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
