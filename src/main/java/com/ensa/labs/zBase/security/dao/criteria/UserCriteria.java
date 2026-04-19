package com.ensa.labs.zBase.security.dao.criteria;

import com.ensa.labs.zBase.security.bean.enums.UserStatus;

import java.util.List;

public record UserCriteria(
        String id,
        String firstName,
        String lastName,
        String email,
        String password,
        Boolean enabled,
        UserStatus status,
        List<RoleCriteria> rolesCriteria
) {
}
