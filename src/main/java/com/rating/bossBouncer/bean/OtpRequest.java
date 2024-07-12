package com.rating.bossbouncer.bean;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class OtpRequest {

    @NotEmpty(message = "Email is required!")
    private String email;
    @NotEmpty(message = "OTP is required!")
    private String otp;
}
