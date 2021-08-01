package com.lwdHouse.learnjava.service.rabbitMQ;

import com.lwdHouse.learnjava.service.rabbitMQ.messaging.LoginMessage;
import com.lwdHouse.learnjava.service.rabbitMQ.messaging.RegistrationMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * rabbit producer
 */
@Component
public class RabbitMsgService {
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendRegistrationMessage(RegistrationMessage msg){
        rabbitTemplate.convertAndSend("registration", "", msg);
    }

    public void sendLoginMessage(LoginMessage msg){
        // 当Routing Key 为 login_failed，此时，只有 q_sms 会收到消息
        String routingKey = msg.success ? "" : "login_failed";
        rabbitTemplate.convertAndSend("login", routingKey, msg);
    }
}
