package com.ensa.labs.zBase.security.service.facade;

import com.ensa.labs.zBase.security.bean.Role;
import com.ensa.labs.zBase.security.bean.User;

import java.util.List;
import java.util.Set;

public interface UserService {

    List<User> findUsersByRoles(Set<Role> roles);

    User loadUserByUsername(String username);

    User loadAuthenticatedUser();

    User createUser(String username, String password);

    User save(User user);

    User saveWithAssociatedEmployee(User user);

    User update(User user);

    User updatedWithAssociatedEmployee(User user);

    void delete(String id);

    User findById(String id);

    List<User> findAll();

    void assignRoleToUser(String username, String roleName);

    User updatePassword(String username, String newPassword);

    User updatePasswordBasedOnCurrentPassword(String username, String currentPassword, String newPassword);

    void updateLastLogin(String username);
}
