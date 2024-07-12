package com.rating.bossbouncer.bean;

import com.rating.bossbouncer.entity.User;
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
