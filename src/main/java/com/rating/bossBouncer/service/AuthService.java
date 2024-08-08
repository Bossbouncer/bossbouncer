package com.rating.bossbouncer.service;

import com.rating.bossbouncer.bean.JwtResponse;
import com.rating.bossbouncer.entity.User;
import com.rating.bossbouncer.exceptions.BadRequestException;
import com.rating.bossbouncer.repository.UserRepository;
import com.rating.bossbouncer.utility.EmailUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private OtpService otpService;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private UserRepository userRepository;

    public void generateOtp(String email) throws MessagingException {
        String otp=otpService.generateOtp(email);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        emailUtil.sendOtpEmail(email,otp, user.getFirstName());
    }

    public ResponseEntity<?> validateOtp(String email, String otp) {
        boolean isValid = otpService.validateOtp(email, otp);
        if (isValid) {
            User user= userRepository.findByEmail(email);
            if(null!=user && !user.getIsVerified()){
                user.setIsVerified(true);
                userRepository.save(user);
            }
            String token = jwtService.generateToken(email);
            return ResponseEntity.ok(new JwtResponse(token,"OTP verified successfully!"));
        } else {
            throw new BadRequestException("Invalid OTP.");
        }
    }
}
