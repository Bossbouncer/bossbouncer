package com.rating.bossbouncer.utility;

import com.rating.bossbouncer.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String to, String otp, String userName) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setFrom("chiefbouncer@bossbouncer.io");
        helper.setSubject("Your OTP Code");

        String htmlMsg = "<div style='font-family: Arial, sans-serif; margin: 0; padding: 0;'>"
                + "<div style='background-color: #EAE8E7; padding: 20px; text-align: center;'>"
                + "<h1 style='margin: 0; color: #333;'>BossBouncer</h1>"
                + "</div>"
                + "<div style='padding: 20px;'>"
                + "<p>Hey <b>" + userName + "</b>,</p>"
                + "<p>Your OTP to verify the account in BossBouncer is - <b>" + otp + "</b></p>"
                + "<p>The OTP is valid for the next 5 minutes only.</p>"
                + "<p>Regards,<br>BossBouncer</p>"
                + "</div>"
                + "<div style='background-color: #EAE8E7; padding: 20px; text-align: center;'>"
                + "<p style='margin: 0;'>&copy; 2024 BossBouncer. All rights reserved.</p>"
                + "</div>"
                + "</div>";

        helper.setText(htmlMsg, true);

        mailSender.send(message);
    }

    public void sendOtpToConfirmRating(User user, String otp) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(user.getEmail());
        helper.setFrom("chiefbouncer@bossbouncer.io");
        helper.setSubject("Your OTP Code");

        String htmlMsg = "<div style='font-family: Arial, sans-serif; margin: 0; padding: 0;'>"
                + "<div style='background-color: #EAE8E7; padding: 20px; text-align: center;'>"
                + "<h1 style='margin: 0; color: #333;'>BossBouncer</h1>"
                + "</div>"
                + "<div style='padding: 20px;'>"
                + "<p>Hey <b>" + user.getFirstName() + "</b>,</p>"
                + "<p>Your OTP to verify the rating in BossBouncer is - <b>" + otp + "</b></p>"
                + "<p>The OTP is valid for the next 5 minutes only.</p>"
                + "<p>Regards,<br>BossBouncer</p>"
                + "</div>"
                + "<div style='background-color: #EAE8E7; padding: 20px; text-align: center;'>"
                + "<p style='margin: 0;'>&copy; 2024 BossBouncer. All rights reserved.</p>"
                + "</div>"
                + "</div>";

        helper.setText(htmlMsg, true);

        mailSender.send(message);
    }
}
