package com.rating.bossBouncer.controller;

import com.rating.bossBouncer.bean.*;
import com.rating.bossBouncer.entity.User;
import com.rating.bossBouncer.exceptions.BadRequestException;
import com.rating.bossBouncer.repository.UserRepository;
import com.rating.bossBouncer.security.TokenProvider;
import com.rating.bossBouncer.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        // Creating user's account
        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setProvider(AuthProvider.local);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User result = userRepository.save(user);

        //Send registration email
        sendEmail(result);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully@"));
    }

    private void sendEmail(User result) {
        String to=result.getEmail();
        String from="noreply@bossbouncer.com";
        String subject= "Boss Bouncer Registration Successful";
        String userName= result.getName();
        String htmlBody = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>...</head>\n" +
                "<body>\n" +
                "<div class=\"container\">\n" +
                "<h2>Registration Successful!</h2>\n" +
                "<p>Dear " + userName + ",</p>\n" +
                "<p>Congratulations! You have successfully registered on Boss Bouncer. ...\n" +
                "<p style=\"text-align: center;\"><a href=\"https://www.bossbouncer.com\" class=\"button\">Visit Boss Bouncer</a></p>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";


        emailService.sendEmail(to,from, subject,htmlBody);
    }

}
