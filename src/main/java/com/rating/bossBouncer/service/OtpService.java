package com.rating.bossbouncer.service;

import com.rating.bossbouncer.security.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {


    private final Map<String, OtpEntry> otpCache = new ConcurrentHashMap<>();
    private final JavaMailSender mailSender;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final AppProperties appProperties;
    //private final long OTP_EXPIRATION_TIME = appProperties.getAuth().getOtpExpirationMesc(); // 5 minutes in milliseconds

    @Autowired
    public OtpService(JavaMailSender mailSender,AppProperties appProperties ) {

        this.mailSender = mailSender;
        this.appProperties=appProperties;
    }

    public String generateOtp(String email) {
        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        OtpEntry otpEntry = new OtpEntry(otp, System.currentTimeMillis());
        otpCache.put(email, otpEntry);

        // Schedule removal of the OTP after 30 minutes
        scheduler.schedule(() -> otpCache.remove(email), 30, TimeUnit.MINUTES);

        System.out.println("OTP: " + otp);
        return otp;
    }

    public boolean validateOtp(String email, String otp) {
        OtpEntry otpEntry = otpCache.get(email);
        if (otpEntry == null) {
            return false;
        }
        if (System.currentTimeMillis() - otpEntry.getCreationTime() > appProperties.getAuth().getOtpExpirationMesc()) {
            otpCache.remove(email);
            return false;
        }
        return otp.equals(otpEntry.getOtp());
    }

    // Class to hold OTP and its creation time
    private static class OtpEntry {
        private final String otp;
        private final long creationTime;

        public OtpEntry(String otp, long creationTime) {
            this.otp = otp;
            this.creationTime = creationTime;
        }

        public String getOtp() {
            return otp;
        }

        public long getCreationTime() {
            return creationTime;
        }
    }
}

