package com.rating.bossBouncer.bean;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDTO {
    @NotBlank
    private String name;

    @NotBlank
    private String imageUrl;

    @NotBlank
    private String email;

    @NotBlank
    private Boolean emailVerified;

    @NotBlank
    private AuthProvider provider;

    @NotBlank
    private String providerId;

    @NotBlank
    private String password;
}

