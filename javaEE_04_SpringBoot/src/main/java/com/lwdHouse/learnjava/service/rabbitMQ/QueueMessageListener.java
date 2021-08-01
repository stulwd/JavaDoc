package com.lwdHouse.learnjava.service.rabbitMQ;

import com.lwdHouse.learnjava.service.rabbitMQ.messaging.LoginMessage;
import com.lwdHouse.learnjava.service.rabbitMQ.messaging.RegistrationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * rabbit consumer
 */
@Component
public class QueueMessageListener {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    static final String QUEUE_MAIL = "q_mail";
    static final String QUEUE_SMS = "q_sms";
    static final String QUEUE_APP = "q_app";

    @RabbitListener(queues = QUEUE_MAIL)
    public void onRegistrationMessageFromMailQueue(RegistrationMessage msg){
        logger.info("queue {} received registration message: {}", QUEUE_MAIL, msg);
    }

    @RabbitListener(queues = QUEUE_SMS)
    public void onRegistrationMessageFromSmsQueue(RegistrationMessage msg){
        logger.info("queue {} received registration message: {}", QUEUE_SMS, msg);
    }

    @RabbitListener(queues = QUEUE_MAIL)
    public void onLoginMessageFromMailQueue(LoginMessage msg){
        logger.info("queue {} received login message: {}", QUEUE_MAIL, msg);
    }

    @RabbitListener(queues = QUEUE_SMS)
    public void onLoginMessageFromSmsQueue(LoginMessage msg){
        logger.info("queue {} received login message: {}", QUEUE_SMS, msg);
    }

    @RabbitListener(queues = QUEUE_APP)
    public void onLoginMessageFromAppQueue(LoginMessage msg){
        logger.info("queue {} received login message: {}", QUEUE_APP, msg);
    }
}
