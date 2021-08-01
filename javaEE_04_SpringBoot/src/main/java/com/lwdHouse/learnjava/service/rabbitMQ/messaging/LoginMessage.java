package com.lwdHouse.learnjava.service.rabbitMQ.messaging;

public class LoginMessage extends AbstractMessaging{

    public boolean success;

    public static LoginMessage of(String email, String name, boolean success){
        var msg = new LoginMessage();
        msg.email = email;
        msg.name = name;
        msg.success = success;
        msg.timestamp = System.currentTimeMillis();
        return msg;
    }

    @Override
    public String toString() {
        return "LoginMessage{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", timestamp=" + timestamp +
                ", success=" + success +
                "} " + super.toString();
    }
}
