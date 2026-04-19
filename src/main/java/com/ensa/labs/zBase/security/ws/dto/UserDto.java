package com.ensa.labs.zBase.security.ws.dto;

import com.ensa.labs.zBase.security.bean.enums.UserStatus;

import java.util.List;


public record UserDto(
        String id,
        String firstName,
        String lastName,
        String email,
        String password,
        String phoneNumber,
        boolean enabled,
        UserStatus status,
        String lastLogin,
        List<RoleDto> roleDtos
) {
}