package com.lwdHouse.learnjava.web.jms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwdHouse.learnjava.web.mail.MailMessage;
import com.lwdHouse.learnjava.web.mail.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

/**
 * JMS--Consumer
 */

/**
 * 为了简化代码，我们直接编写一个方法，给一个方法标注上JmsListener，spring就可以调用这个方法处理消息
 * 传统的方法是直接调用JMS的API来处理消息，那么编写的代码大致如下
 * // 创建JMS连接:
 * Connection connection = connectionFactory.createConnection();
 * // 创建会话:
 * Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
 * // 创建一个Consumer:
 * MessageConsumer consumer = session.createConsumer(queue);
 * // 为Consumer指定一个消息处理器:
 * consumer.setMessageListener(new MessageListener() {
 *     public void onMessage(Message message) {
 *         // 在此处理消息...
 *     }
 * });
 * // 启动接收消息的循环:
 * connection.start();
 */

@Component
public class MailMessageListener {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MailService mailService;

    // destination表示接受的消息来源，concurrency表示可以最多同时并发处理的消息个数
    // Spring在通过MessageListener接收到消息后，
    // 并不是直接调用mailMessageListener.onMailMessageReceived()，
    // 而是用线程池调用，因此，要时刻牢记，onMailMessageReceived()方法可能被多线程并发执行，一定要保证线程安全
    @JmsListener(destination = "jms/queue/mail", concurrency = "10")
    public void onMailMessageReceived(Message message) throws JMSException, JsonProcessingException {
        logger.info("received message: " + message);
        if (message instanceof TextMessage){
            String text = ((TextMessage) message).getText();
            MailMessage mm = objectMapper.readValue(text, MailMessage.class);
            mailService.sendRegistrationMailFromMQ(mm);
        }else {
            logger.error("unable to process non-text message!");
        }
    }
}
