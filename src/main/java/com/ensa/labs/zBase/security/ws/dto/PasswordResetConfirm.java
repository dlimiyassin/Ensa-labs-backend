package com.ensa.labs.zBase.security.ws.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** New password chosen from the link received by e-mail. */
public record PasswordResetConfirm(@NotBlank @Size(max = 200) String token,
                                   @NotBlank @Size(max = 200) String newPassword) {
}
