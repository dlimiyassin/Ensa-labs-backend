package com.ensa.labs.zBase.security.ws.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String email;
    private String password;
}
