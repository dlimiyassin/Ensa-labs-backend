package com.ensa.labs.zBase.security.dao.facade;

import com.ensa.labs.zBase.security.bean.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface RoleDao extends JpaRepository<Role, String>, JpaSpecificationExecutor<Role> {
    Optional<Role> findByName(String name);
}