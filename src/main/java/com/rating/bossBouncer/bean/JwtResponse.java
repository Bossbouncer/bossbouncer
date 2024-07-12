package com.rating.bossbouncer.bean;

import lombok.Data;

@Data
public class JwtResponse {
    private String accessToken;
    private String message;

    public JwtResponse(String accessToken, String message) {
        this.accessToken=accessToken;
        this.message=message;
    }
}
