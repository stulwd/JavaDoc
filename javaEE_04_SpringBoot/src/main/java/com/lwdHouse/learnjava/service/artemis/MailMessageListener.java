package com.lwdHouse.learnjava.service.artemis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwdHouse.learnjava.service.mail.MailMessage;
import com.lwdHouse.learnjava.service.mail.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

/**
 * jms consumer
 */
@Component
public class MailMessageListener {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MailService mailService;

    @JmsListener(destination = "jms/queue/mail", concurrency = "10")
    public void onMailMessageReceived(Message message) throws JMSException, JsonProcessingException {
        logger.info("received message: " + message);
        if (message instanceof TextMessage){
            String text = ((TextMessage) message).getText();
            MailMessage mailMessage = objectMapper.readValue(text, MailMessage.class);
            mailService.sendRegistrationMailFromMQ(mailMessage);
        }else {
            logger.error("unable to process non-text message!");
        }
    }
}
