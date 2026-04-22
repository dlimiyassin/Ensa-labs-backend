package com.ensa.labs.recherche.service;

import com.ensa.labs.exception.ResourceNotFoundException;
import com.ensa.labs.recherche.dto.UserDTO;
import com.ensa.labs.recherche.mapper.UserMapper;
import com.ensa.labs.zBase.security.bean.User;
import com.ensa.labs.zBase.security.dao.facade.RoleDao;
import com.ensa.labs.zBase.security.dao.facade.UserDao;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class UserManagementService {
    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserManagementService(UserDao userDao, RoleDao roleDao, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public List<UserDTO> findAll() { return userDao.findAll().stream().map(userMapper::toDto).toList(); }
    public UserDTO findById(String id) { return userMapper.toDto(get(id)); }

    public UserDTO create(UserDTO dto) {
        User user = userMapper.toEntity(dto);
        if (user.getCin() != null && !user.getCin().isBlank()) {
            user.setPassword(passwordEncoder.encode(user.getCin()));
        } else {
            user.setPassword(passwordEncoder.encode("ChangeMe123!"));
        }
        applyRelations(user, dto);
        return userMapper.toDto(userDao.save(user));
    }

    public UserDTO update(String id, UserDTO dto) {
        User user = get(id);
        user.setUsername(dto.username());
        user.setCin(dto.cin());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setPhoneNumber(dto.phoneNumber());
        user.setEnabled(dto.enabled());
        user.setStatus(dto.status());
        applyRelations(user, dto);
        return userMapper.toDto(userDao.save(user));
    }

    public void delete(String id) { userDao.delete(get(id)); }

    private void applyRelations(User user, UserDTO dto) {
        user.setRoles(new HashSet<>(dto.roleIds().stream().map(roleId -> roleDao.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId))).toList()));
    }

    private User get(String id) { return userDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id)); }
}
