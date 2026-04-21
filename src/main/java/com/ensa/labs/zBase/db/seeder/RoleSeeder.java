package com.ensa.labs.zBase.db.seeder;

import com.ensa.labs.zBase.security.bean.Role;
import com.ensa.labs.zBase.security.dao.facade.RoleDao;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RoleSeeder {

    private final RoleDao roleDao;

    public RoleSeeder(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public Map<String, Role> seed() {
        Map<String, Role> roles = new HashMap<>();

        roles.put("ADMIN", createIfMissing("ADMIN"));
        roles.put("RESEARCHER", createIfMissing("RESEARCHER"));

        return roles;
    }

    private Role createIfMissing(String name) {
        return roleDao.findByName(name)
                .orElseGet(() -> roleDao.save(new Role(name)));
    }
}
