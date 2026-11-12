package com.ensa.labs.zBase.security.ws.dto;

import lombok.Data;

@Data
public class JWTAuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    /** Access token lifetime in seconds. */
    private long expiresIn;

    public JWTAuthResponse(String accessToken, String refreshToken, long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }
}
