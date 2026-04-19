package com.ensa.labs.zBase.security.ws.dto;

import lombok.Data;

@Data
public class RegisterDto {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String cin;
    private String phoneNumber;
}
