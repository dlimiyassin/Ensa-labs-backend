package com.ensa.labs.zBase.security.service.facade;

import com.ensa.labs.zBase.security.ws.dto.JWTAuthResponse;
import com.ensa.labs.zBase.security.ws.dto.LoginDto;
import com.ensa.labs.zBase.security.ws.dto.RegisterDto;

public interface AuthService {
    JWTAuthResponse login(LoginDto loginDto);

    JWTAuthResponse refresh(String refreshToken);

    String register(RegisterDto registerDto);
}
