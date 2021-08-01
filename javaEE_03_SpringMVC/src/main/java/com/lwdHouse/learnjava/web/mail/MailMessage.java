package com.lwdHouse.learnjava.web.mail;

public class MailMessage {

    public static enum Type{
        REGISTRATION, SIGNIN;
    }

    public Type type;
    public Boolean html;
    public String from;
    public String to;
    public String subject;
    public String name;
    public String text;
    public long timestamp;

    public static MailMessage registration(String to, String name){
        MailMessage msg = new MailMessage();
        msg.type = Type.REGISTRATION;
        msg.from = "stulwd@163.com";
        msg.to = to;
        msg.subject = "Welcome to Java course!";
        msg.name = name;
        msg.text = "<p>Hi, %s,</p><p>Welcome to Java course!</p><p>Send at %s</p>";
        msg.html = true;
        msg.timestamp = System.currentTimeMillis();
        return msg;
    }
}
