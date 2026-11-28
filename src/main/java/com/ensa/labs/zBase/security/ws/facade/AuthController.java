package com.ensa.labs.zBase.security.ws.facade;

import com.ensa.labs.zBase.security.service.facade.AuthService;
import com.ensa.labs.zBase.security.service.impl.PasswordResetService;
import com.ensa.labs.zBase.security.ws.dto.JWTAuthResponse;
import com.ensa.labs.zBase.security.ws.dto.LoginDto;
import com.ensa.labs.zBase.security.ws.dto.PasswordResetConfirm;
import com.ensa.labs.zBase.security.ws.dto.PasswordResetRequest;
import com.ensa.labs.zBase.security.ws.dto.RefreshTokenRequest;
import com.ensa.labs.zBase.security.ws.dto.RegisterDto;
import com.ensa.labs.zBase.security.ws.dto.RegisterResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final PasswordResetService passwordResetService;

    public AuthController(AuthService authService, PasswordResetService passwordResetService) {
        this.authService = authService;
        this.passwordResetService = passwordResetService;
    }

    /**
     * Sends a reset link to the address when it belongs to an active account. The answer is the
     * same whether the address is known or not.
     */
    @PostMapping("/password-reset/request")
    public ResponseEntity<Void> requestPasswordReset(@Valid @RequestBody PasswordResetRequest request,
                                                     HttpServletRequest httpRequest) {
        passwordResetService.requestReset(request.email(), clientAddress(httpRequest));
        return ResponseEntity.accepted().build();
    }

    /** Sets the new password chosen from the link received by e-mail. */
    @PostMapping("/password-reset/confirm")
    public ResponseEntity<Void> confirmPasswordReset(@Valid @RequestBody PasswordResetConfirm request) {
        passwordResetService.resetPassword(request.token(), request.newPassword());
        return ResponseEntity.noContent().build();
    }

    /** Client address as seen by nginx (X-Real-IP), the direct peer otherwise. */
    private static String clientAddress(HttpServletRequest request) {
        String realIp = request.getHeader("X-Real-IP");
        return realIp != null && !realIp.isBlank() ? realIp.trim() : request.getRemoteAddr();
    }

    @PostMapping("/login")
    public JWTAuthResponse login(@Valid @RequestBody LoginDto loginDto) {
        return authService.login(loginDto);
    }

    @PostMapping("/refresh")
    public JWTAuthResponse refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return authService.refresh(request.refreshToken());
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterDto registerDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RegisterResponse(authService.register(registerDto)));
    }
}
