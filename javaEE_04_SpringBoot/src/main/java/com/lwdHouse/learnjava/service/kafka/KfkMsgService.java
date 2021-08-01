package com.lwdHouse.learnjava.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwdHouse.learnjava.service.rabbitMQ.messaging.LoginMessage;
import com.lwdHouse.learnjava.service.rabbitMQ.messaging.RegistrationMessage;
import com.zaxxer.hikari.HikariConfig;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * kafka producer
 */

@Component
public class KfkMsgService {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    KafkaTemplate kafkaTemplate;


    public void sendRegistrationMessage(RegistrationMessage msg) throws JsonProcessingException {
        send("topic_registration", msg);
    }

    public void sendLoginMessage(LoginMessage msg) throws JsonProcessingException {
        send("topic_login", msg);
    }

    private void send(String topic, Object msg) throws JsonProcessingException {
        ProducerRecord<String, String> pr = new ProducerRecord<>(topic, objectMapper.writeValueAsString(msg));
        pr.headers().add("type", msg.getClass().getName().getBytes(StandardCharsets.UTF_8));
        kafkaTemplate.send(pr);
    }
}
