package com.lwdHouse.learnjava.service.rabbitMQ.messaging;

public class RegistrationMessage extends AbstractMessaging{

    public static RegistrationMessage of(String email, String name){
        RegistrationMessage msg = new RegistrationMessage();
        msg.email = email;
        msg.name = name;
        msg.timestamp = System.currentTimeMillis();
        return msg;
    }

    @Override
    public String toString() {
        return "RegistrationMessage{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", timestamp=" + timestamp +
                "} " + super.toString();
    }
}
