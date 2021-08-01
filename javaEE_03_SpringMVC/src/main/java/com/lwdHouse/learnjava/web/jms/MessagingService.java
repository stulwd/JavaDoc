package com.lwdHouse.learnjava.web.jms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwdHouse.learnjava.web.mail.MailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * JMS--Producer
 */
// 现在我们用的是JMS2.0版本，简化了代码
// 在JMS1.1中Producer向MQ发送消息的代码比较复杂，如下
//    try {
//            Connection connection = null;
//            try {
//            // 创建连接:
//            connection = connectionFactory.createConnection();
//            // 创建会话:
//            Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
//            // 创建一个Producer并关联到某个Queue:
//            MessageProducer messageProducer = session.createProducer(queue);
//            // 创建一个文本消息:
//            TextMessage textMessage = session.createTextMessage(text);
//            // 发送消息:
//            messageProducer.send(textMessage);
//            } finally {
//            // 关闭连接:
//            if (connection != null) {
//            connection.close();
//            }
//            }
//            } catch (JMSException ex) {
//            // 处理JMS异常
//            }
@Component
public class MessagingService {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    JmsTemplate jmsTemplate;

    public void sendMailMessage(MailMessage msg) throws JsonProcessingException {
        String text = objectMapper.writeValueAsString(msg);
        // Artemis消息服务器默认配置下会自动创建Queue，因此不必手动创建一个名为jms/queue/mail的Queue，
        // 但不是所有的消息服务器都会自动创建Queue，生产环境的消息服务器通常会关闭自动创建功能，需要手动创建Queue
        jmsTemplate.send("jms/queue/mail", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(text);
            }
        });
    }
}
