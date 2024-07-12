package com.rating.bossbouncer.controller;

import com.rating.bossbouncer.service.AuthService;
import com.rating.bossbouncer.service.JwtService;
import com.rating.bossbouncer.service.OtpService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final OtpService otpService;
    private final JwtService jwtService;
    private final AuthService authService;

    @Autowired
    public AuthController(OtpService otpService, JwtService jwtService, AuthService authService) {
        this.otpService = otpService;
        this.jwtService = jwtService;
        this.authService=authService;
    }

    @PostMapping("/requestLoginOtp")
    public ResponseEntity<String> requestOtp(@RequestParam String email) throws MessagingException {
        authService.generateOtp(email);
        return ResponseEntity.ok("OTP sent to your email.");
    }

    @PostMapping("/validateLoginOtp")
    public ResponseEntity<?> validateOtp(@RequestParam String email, @RequestParam String otp) {
      return authService.validateOtp(email, otp);
    }

}

