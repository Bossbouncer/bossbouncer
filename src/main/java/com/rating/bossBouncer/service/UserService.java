package com.rating.bossBouncer.service;

import com.rating.bossBouncer.entity.User;
import com.rating.bossBouncer.exceptions.BadRequestException;
import com.rating.bossBouncer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public void generatePasswordResetToken(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Generate a unique token
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiration(LocalDateTime.now().plusHours(24));

        userRepository.save(user);

        // Send reset password email
        sendPasswordResetEmail(user.getEmail(), token);
    }

    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetTokenAndResetTokenExpirationAfter(token, LocalDateTime.now())
                .orElseThrow(() -> new BadRequestException("Invalid or expired token"));

        // Update password and clear reset token
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiration(null);

        userRepository.save(user);
    }

    private void sendPasswordResetEmail(String toEmail, String resetToken) {
        String to=toEmail;
        String from="chiefbouncer@bossbouncer.io";
        String subject= "Boss Bouncer: Password Reset Request";
        String htmlBody = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>...</head>\n" +
                "<body>\n" +
                "<div class=\"container\">\n" +
                "<h2>Password Reset Request</h2>\n" +
                "<p>Dear User,</p>\n" +
                "<p>We received a request to reset your password. If you did not make this request, please ignore this email.</p>\n" +
                "<p>To reset your password, click the following link:</p>\n" +
                "<p><a href=\"https://www.bossbouncer.com/reset-password?token=" + resetToken + "\">Reset Password</a></p>\n" +
                "<p>This link will expire in 24 hours for security reasons.</p>\n" +
                "<p>If you have any questions or need assistance, please contact our support team.</p>\n" +
                "<p>Best regards,<br> Boss Bouncer</p>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";

        emailService.sendEmail(to, from, subject,htmlBody);
    }

    public void verifyEmail(String email, String verificationCode) {
        User user = userRepository.findByEmail(email);
        if (user != null && verificationCode.equals(user.getEmailVerificationCode())) {
            // mark the user as verified
            user.setEmailVerified(true);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Invalid verification code");
        }
    }
}

