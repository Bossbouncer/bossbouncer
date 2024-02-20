package com.rating.bossBouncer.controller;

import com.rating.bossBouncer.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public String sendEmail(@RequestParam String to, @RequestParam String from, @RequestParam String subject, @RequestParam String body) {
        emailService.sendEmail(to, from, subject, body);
        return "Email sent successfully!";
    }
}

