package com.rating.bossBouncer.controller;

import com.rating.bossBouncer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/forgot-password")
public class ForgotPasswordController {

    @Autowired
    private UserService userService;

    @PostMapping("/generate-token")
    public ResponseEntity<String> generatePasswordResetToken(@RequestParam String email) {
        userService.generatePasswordResetToken(email);
        return ResponseEntity.ok("Password reset token sent successfully");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        userService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password reset successfully");
    }
}

