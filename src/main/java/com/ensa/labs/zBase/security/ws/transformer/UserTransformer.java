package com.ensa.labs.zBase.security.ws.transformer;

import com.ensa.labs.zBase.security.bean.User;
import com.ensa.labs.zBase.security.ws.dto.UserDto;
import com.ensa.labs.zBase.transformer.AbstractTransformer;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;

@Component
public class UserTransformer extends AbstractTransformer<User, UserDto> {

    private final RoleTransformer roleTransformer;

    public UserTransformer(RoleTransformer roleTransformer) {
        this.roleTransformer = roleTransformer;
    }

    @Override
    public User toEntity(UserDto dto) {
        if (dto == null) return null;
        User user = new User();
        user.setId(dto.id());
        user.setUsername(dto.username());
        user.setCin(dto.cin());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setEnabled(dto.enabled());
        user.setStatus(dto.status());
        if (dto.lastLogin() != null) user.setLastLogin(Instant.parse(dto.lastLogin()));
        user.setPhoneNumber(dto.phoneNumber());
        user.setRoles(new HashSet<>(roleTransformer.toEntity(dto.roleDtos())));
        return user;
    }

    @Override
    public UserDto toDto(User entity) {
        if (entity == null) return null;
        return new UserDto(entity.getId(), entity.getUsername(), entity.getCin(), entity.getFirstName(), entity.getLastName(), entity.getEmail(), entity.getPhoneNumber(), entity.isEnabled(), entity.getStatus(), entity.getLastLogin() != null ? entity.getLastLogin().toString() : null, new ArrayList<>(roleTransformer.toDto(entity.getRoles().stream().toList())));
    }
}
