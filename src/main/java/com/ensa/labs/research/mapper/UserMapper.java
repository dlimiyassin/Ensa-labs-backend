package com.ensa.labs.research.mapper;

import com.ensa.labs.research.dto.UserDTO;
import com.ensa.labs.zBase.security.bean.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO toDto(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getCin(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(), user.isEnabled(), user.getStatus(),
                user.getRoles().stream().map(r -> r.getId()).toList(),
                user.getTeams().stream().map(t -> t.getId()).toList());
    }

    public User toEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.id());
        user.setUsername(dto.username());
        user.setCin(dto.cin());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setPhoneNumber(dto.phoneNumber());
        user.setEnabled(dto.enabled());
        user.setStatus(dto.status());
        return user;
    }
}
