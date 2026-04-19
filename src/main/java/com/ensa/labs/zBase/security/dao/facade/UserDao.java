package com.ensa.labs.zBase.security.dao.facade;

import com.ensa.labs.zBase.security.bean.Role;
import com.ensa.labs.zBase.security.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserDao extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findUsersByRoles(Set<Role> roles);
}
