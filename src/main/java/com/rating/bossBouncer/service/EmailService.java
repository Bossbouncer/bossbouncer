package com.rating.bossBouncer.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String from, String subject, String body) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(to);
            helper.setFrom(from);
            helper.setSubject(subject);
            helper.setText(body, true); // Set to true for HTML content

            javaMailSender.send(message);
        } catch (MessagingException e) {
            // Handle exception
            e.printStackTrace();
        }
    }
}

