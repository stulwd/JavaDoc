package com.lwdHouse.learnjava.web.mail;

import com.lwdHouse.learnjava.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;

@Component
public class MailService {
    @Value("${smtp.from}")
    String from;

    @Autowired
    JavaMailSender mailSender;

    public void sendRegistrationMail(User user){
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(from);
            helper.setTo(user.getEmail());
            helper.setSubject("Welcome to Java course!");
            String html = String.format("<p>Hi, %s,</p><p>Welcome to Java course!</p><p>Send at %s</p>",
                    user.getName(), LocalDateTime.now());
            helper.setText(html, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e){
            throw new RuntimeException(e);
        }
    }

    public void sendRegistrationMailFromMQ(MailMessage mm){
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(mm.from);
            helper.setTo(mm.to);
            helper.setSubject(mm.subject);
            String html = String.format(mm.text,
                    mm.name, mm.timestamp);
            helper.setText(html, mm.html);
            mailSender.send(mimeMessage);
        } catch (MessagingException e){
            throw new RuntimeException(e);
        }
    }
}
