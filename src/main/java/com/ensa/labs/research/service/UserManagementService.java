package com.ensa.labs.research.service;

import com.ensa.labs.exception.ResourceNotFoundException;
import com.ensa.labs.research.dao.LabRepository;
import com.ensa.labs.research.dao.TeamRepository;
import com.ensa.labs.research.dto.UserDTO;
import com.ensa.labs.research.mapper.UserMapper;
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
    private final TeamRepository teamRepository;
    private final LabRepository labRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserManagementService(UserDao userDao, RoleDao roleDao, TeamRepository teamRepository, LabRepository labRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.teamRepository = teamRepository;
        this.labRepository = labRepository;
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
        user.setTeams(new HashSet<>(dto.teamIds().stream().map(teamId -> teamRepository.findById(teamId).orElseThrow(() -> new ResourceNotFoundException("Team", "id", teamId))).toList()));
        user.setLabs(new HashSet<>(dto.labIds().stream().map(labId -> labRepository.findById(labId).orElseThrow(() -> new ResourceNotFoundException("Lab", "id", labId))).toList()));
        user.getTeams().forEach(team -> team.getMembers().add(user));
        user.getLabs().forEach(lab -> lab.getUsers().add(user));
    }

    private User get(String id) { return userDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id)); }
}
