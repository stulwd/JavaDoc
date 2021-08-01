package com.lwdHouse.learnjava.service.artemis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwdHouse.learnjava.service.mail.MailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * jms producer
 */
@Component
public class MessagingService {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    JmsTemplate jmsTemplate;

    public void sendMailMessage(MailMessage message) throws JsonProcessingException {
        String text = objectMapper.writeValueAsString(message);
        jmsTemplate.send("jms/queue/mail", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(text);

            }
        });
    }
}
