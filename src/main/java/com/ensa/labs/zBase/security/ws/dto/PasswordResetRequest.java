package com.ensa.labs.zBase.security.ws.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** "Forgot password" form: the e-mail address of the account. */
public record PasswordResetRequest(@NotBlank @Size(max = 150) String email) {
}
