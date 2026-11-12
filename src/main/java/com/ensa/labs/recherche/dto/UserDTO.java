package com.ensa.labs.recherche.dto;

import com.ensa.labs.zBase.security.bean.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * @param roleIds   ids of the authorization roles
 * @param memberId  lab member linked to this account (gives the lab / team scope)
 * @param lastLogin read-only
 * @param password  write-only and optional. On creation, the CIN is used when it is empty.
 */
public record UserDTO(
        String id,
        @NotBlank @Size(max = 80) String username,
        String cin,
        String firstName,
        String lastName,
        @Email String email,
        String phoneNumber,
        boolean enabled,
        UserStatus status,
        @NotEmpty List<String> roleIds,
        String memberId,
        String lastLogin,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) @Size(min = 6, max = 100) String password
) {
}
