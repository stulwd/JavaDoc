package com.lwdHouse;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.*;
import java.util.Properties;

public class part5_SendEmail {
    public static void main(String[] args) throws MessagingException, IOException {
        // 服务器地址
        String smtp = "smtp.163.com";
        // 登录用户名
        String username = "stulwd@163.com";
        // 密码
        String password = "DGSHPUSIZVRPYSKZ";
        // 连接到smtp服务器的465端口
        Properties props = new Properties();
        props.put("mail.smtp.host", smtp);
        props.put("mail.smtp.port", "25");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");     // 启动TLS加密
        // 获取Session实例
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        // 设置debug模式便于调试
        session.setDebug(true);

        // 1.发送邮件
        /* Multipurpose Internet Mail Extensions */
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("stulwd@163.com"));
        /* 还有CC：抄送 BCC: 暗抄送 */
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("1091010717@qq.com"));
        // 设置邮件主题
        message.setSubject("Hello", "UTF-8");
        // 设置正文
//        message.setText("Hi xiaoming...", "UTF-8");
        // 如果要发送html, 则
        message.setText("<h1>Hello</h1><p>Hi, xxx</p>", "UTF-8", "html");
        // 发送
        Transport.send(message);

        // 2.发送附件
        sendAccessory(session);
        // 3.发送内嵌图片
        sendEmbeddedImg(session);


    }

    /**
     * 发送附件
     * @param session
     * @throws MessagingException
     * @throws IOException
     */
    private static void sendAccessory(Session session) throws MessagingException, IOException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("stulwd@163.com"));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("1091010717@qq.com"));
        message.setSubject("Hello", "UTF-8");

        Multipart multipart = new MimeMultipart();
        // textpart
        BodyPart textpart = new MimeBodyPart();
        textpart.setContent("<h1>Hello</h1><p>Hi, xxx</p>", "text/html;charset=utf-8");
        // imgpart
        BodyPart imgpart = new MimeBodyPart();
        imgpart.setFileName("pic.jpg");
        InputStream input = new FileInputStream("javaCE_02/src/main/resources/img.jpg");
        imgpart.setDataHandler(new DataHandler(new ByteArrayDataSource(input, "application/octet-stream")));
        // 添加
        multipart.addBodyPart(textpart);
        multipart.addBodyPart(imgpart);
        message.setContent(multipart);
        Transport.send(message);
    }

    /**
     * 内嵌图片
     */
    private static void sendEmbeddedImg(Session session) throws MessagingException, IOException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("stulwd@163.com"));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("1091010717@qq.com"));
        message.setSubject("hello", "UTF-8");

        BodyPart textpart = new MimeBodyPart();
        textpart.setContent("<h1>Hello</h1> <p><img src=\"cid:img01\"></p>", "text/html;charset=utf-8");

        BodyPart imgpart = new MimeBodyPart();
        imgpart.setFileName("pic.jpg");
        imgpart.setDataHandler(new DataHandler(new ByteArrayDataSource(new FileInputStream("javaCE_02/src/main/resources/img.jpg"),
                                                                                            "application/octet-stream")));
        // 给图片设置cid
        imgpart.setHeader("Content-ID", "<img01>");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(textpart);
        multipart.addBodyPart(imgpart);
        message.setContent(multipart);

        Transport.send(message);

    }
}
