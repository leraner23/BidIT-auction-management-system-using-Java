package com.project.BidIT.Service;

import com.project.BidIT.Service.User.UserService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String senderEmail;
    private final UserService userService;
    private final JavaMailSender javaMailSender;

    public EmailService( UserService userService, JavaMailSender javaMailSender) {

        this.userService = userService;
        this.javaMailSender = javaMailSender;
    }




    String link = "http://localhost:8080/";
//    @RequestMapping("/user/email")
    public void sendEmail(String winnerEmail){
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(senderEmail);
            helper.setTo(winnerEmail);
            helper.setSubject("Congratulation!! message");
            String link = "http://localhost:8080/";
            try(var inputstream = Objects.requireNonNull(EmailService.class.getResourceAsStream("/templates/emailMessage.html"))){
                String send = new String(inputstream.readAllBytes(), StandardCharsets.UTF_8);
                send = send.replace("{link}", link);
                send = send.replace("{email}",winnerEmail);
                helper.setText(send,true);
            }
            javaMailSender.send(message);
        }catch (Exception e){
            throw new RuntimeException("message not sent" + e.getMessage());
        }

    }

}
