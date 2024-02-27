package com.rating.bossBouncer.bean;

import com.rating.bossBouncer.entity.User;
import lombok.Data;

@Data
public class AuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private User user;

    public AuthResponse(String accessToken, User user) {
        this.accessToken = accessToken;
        this.user=user;
    }
}
