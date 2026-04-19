package com.ensa.labs.zBase.security.service.impl;


import com.ensa.labs.exception.AuthenticationRequiredException;
import com.ensa.labs.exception.GlobalException;
import com.ensa.labs.exception.ResourceAlreadyExistsException;
import com.ensa.labs.exception.ResourceNotFoundException;
import com.ensa.labs.zBase.security.bean.Role;
import com.ensa.labs.zBase.security.bean.User;
import com.ensa.labs.zBase.security.bean.enums.UserStatus;
import com.ensa.labs.zBase.security.dao.facade.UserDao;
import com.ensa.labs.zBase.security.service.facade.RoleService;
import com.ensa.labs.zBase.security.service.facade.UserService;
import com.ensa.labs.zBase.security.utils.SecurityUtil;
import com.ensa.labs.zBase.util.CollectionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findUsersByRoles(Set<Role> roles) {
        Set<Role> roleSet = new HashSet<>();
        for (Role role : roles) {
            Optional<Role> userRole = Optional.ofNullable(roleService.findByName(role.getName()));
            userRole.ifPresent(roleSet::add);
        }
        return userDao.findUsersByRoles(roleSet);
    }

    @Override
    public User loadUserByEmail(String email) {
        return userDao.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "Email", email)
                );
    }

    @Override
    public User loadAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationRequiredException("No authenticated user found");
        }

        String email = authentication.getName();
        return userDao.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "Email", email)
                );
    }


    @Override
    public User createUser(String email, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        return userDao.save(new User(email, encodedPassword));
    }

    @Override
    public User save(User user) {
        findByEmail(user.getEmail());
        prepareSave(user);
        return userDao.save(user);
    }

    private void prepareSave(User user) {
        Set<Role> roles = new HashSet<>();
        if (CollectionUtil.isNotEmpty(user.getRoles())) {
            for (Role role : user.getRoles()) {
                Role foundedRole = roleService.findByName(role.getName());
                roles.add(foundedRole);
            }
            user.setRoles(roles);
        }
    }

    @Override
    @Transactional
    public User saveWithAssociatedEmployee(User user) {
        findByEmail(user.getEmail());
        String password = SecurityUtil.generatePassword();
        user.setPassword(passwordEncoder.encode(password));
        Set<Role> roles = new HashSet<>();
        for (Role role : user.getRoles()) {
            Optional<Role> userRole = Optional.ofNullable(roleService.findByName(role.getName()));
            userRole.ifPresent(roles::add);
        }
        user.setRoles(roles);

        return userDao.save(user);
    }

    @Override
    @Transactional
    public User updatedWithAssociatedEmployee(User user) {
        User foundedUser = findById(user.getId());
        user.setPassword(foundedUser.getPassword());
        return userDao.save(user);
    }

    @Override
    public User update(User user) {
        User found = findById(user.getId());

        found.setFirstName(user.getFirstName());
        found.setLastName(user.getLastName());
        found.setPhoneNumber(user.getPhoneNumber());
        found.setEnabled(user.isEnabled());

        if (found.isEnabled()) {
            found.setStatus(UserStatus.ACTIF);
        } else {
            found.setStatus(UserStatus.BLOQUE);
        }

        return userDao.save(found);
    }


    @Override
    public void delete(String id) {
        findById(id);
        userDao.deleteById(id);
    }

    @Override
    public User findById(String id) {
        return userDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    private void findByEmail(String email) {
        userDao.findByEmail(email).ifPresent(user -> {
            throw new ResourceAlreadyExistsException("User", "email", email);
        });
    }

    @Override
    public void assignRoleToUser(String email, String roleName) {
        User user = loadUserByEmail(email);
        Role role = roleService.findByName(roleName);
        user.getRoles().add(role);
    }

    @Override
    public User updatePassword(String email, String newPassword) {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Email", email));
        user.setPassword(passwordEncoder.encode(newPassword));
        return userDao.save(user);
    }

    @Override
    public User updatePasswordBasedOnCurrentPassword(String email, String currentPassword, String newPassword) {
        User foundedUser = userDao.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", "Email", email));
        if (passwordEncoder.matches(currentPassword, foundedUser.getPassword())) {
            foundedUser.setPassword(passwordEncoder.encode(newPassword));
            return userDao.save(foundedUser);
        } else {
            throw new GlobalException(HttpStatus.CONFLICT, "Current password is incorrect");
        }
    }

    @Override
    public void updateLastLogin(String email) {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        user.setLastLogin(Instant.now());
        userDao.save(user);
    }


}
