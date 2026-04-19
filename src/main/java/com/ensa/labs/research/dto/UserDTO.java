package com.ensa.labs.research.dto;

import com.ensa.labs.zBase.security.bean.enums.UserStatus;

import java.util.List;

public record UserDTO(
        String id,
        String username,
        String cin,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        boolean enabled,
        UserStatus status,
        List<String> roleIds,
        List<String> teamIds,
        List<String> labIds
) {
}
