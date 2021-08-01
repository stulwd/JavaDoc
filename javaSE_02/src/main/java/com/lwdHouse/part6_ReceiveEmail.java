package com.lwdHouse;

import com.sun.mail.pop3.POP3SSLStore;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * 接受Email
 */
public class part6_ReceiveEmail {
    public static void main(String[] args) throws MessagingException, UnsupportedEncodingException {
        String host = "pop.163.com";
        int port = 995;
        String username = "stulwd@163.com";
        String password = "DGSHPUSIZVRPYSKZ";

        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "pop3");       // 和props.put一模一样
        props.setProperty("mail.pop3.host", host);
        props.setProperty("mail.pop3.port", String.valueOf(port));  // 端口号
        // 启动SSL
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.port", String.valueOf(port));

        // url = "pop3://stulwd@163.com:DGSHPUSIZVRPYSKZ@pop.163.com:995"
        URLName url = new URLName("pop3", host, port, "", username, password);
        // 获取session
        Session session = Session.getInstance(props, null);
        session.setDebug(true);
        // 连接到store,一个store对象表示整个邮箱的存储
        Store store = new POP3SSLStore(session, url);
        store.connect();

        // 获取收件箱
        Folder folder = store.getFolder("INBOX");
        // 以读写方式打开
        folder.open(Folder.READ_WRITE);
        System.out.println("Total messages: "+folder.getMessageCount());
        System.out.println("new messages: "+folder.getNewMessageCount());
        System.out.println("Unread messages: "+folder.getUnreadMessageCount());
        System.out.println("Deleted messages: "+folder.getDeletedMessageCount());

        Message[] messages = folder.getMessages();
        for (Message message : messages) {
            printMessage((MimeMessage) message);
        }

        folder.close(true);         // 传入true表示删除操作会同步到服务器上
        store.close();
    }

    private static void printMessage(MimeMessage msg) throws MessagingException, UnsupportedEncodingException {
        System.out.println("subject: "+ MimeUtility.decodeText(msg.getSubject()));
        Address[] froms = msg.getFrom();
        InternetAddress address = (InternetAddress) froms[0];
        String personal = address.getPersonal();
        String from = personal == null ? address.getAddress() : (MimeUtility.decodeText(personal) + " <" + address.getAddress() + ">");
        System.out.println("from: " + from);

    }

//    这个好像有问题
//    private String getBody(Part part) throws MessagingException, IOException {
//        if (part.isMimeType("text/*")) {
//            // Part是文本:
//            return part.getContent().toString();
//        }
//        if (part.isMimeType("multipart/*")) {
//            // Part是一个Multipart对象:
//            Multipart multipart = (Multipart) part.getContent();
//            // 循环解析每个子Part:
//            for (int i = 0; i < multipart.getCount(); i++) {
//                BodyPart bodyPart = multipart.getBodyPart(i);
//                String body = getBody(bodyPart);
//                if (!body.isEmpty()) {
//                    return body;
//                }
//            }
//        }
//        return "";
//    }
}
