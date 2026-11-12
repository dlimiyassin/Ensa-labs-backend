package com.ensa.labs.recherche.mapper;

import com.ensa.labs.recherche.dao.MemberRepository;
import com.ensa.labs.recherche.dto.UserDTO;
import com.ensa.labs.zBase.security.bean.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final MemberRepository memberRepository;

    public UserMapper(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public UserDTO toDto(User user) {
        String memberId = user.getId() == null ? null
                : memberRepository.findByUserId(user.getId()).map(m -> m.getId()).orElse(null);
        return new UserDTO(user.getId(), user.getUsername(), user.getCin(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(), user.isEnabled(), user.getStatus(),
                user.getRoles().stream().map(r -> r.getId()).toList(),
                memberId,
                user.getLastLogin() != null ? user.getLastLogin().toString() : null,
                null);
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
        if (dto.status() != null) {
            user.setStatus(dto.status());
        }
        return user;
    }
}
