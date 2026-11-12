package com.ensa.labs.zBase.db.seeder;

import com.ensa.labs.zBase.security.bean.Role;
import com.ensa.labs.zBase.security.constant.Roles;
import com.ensa.labs.zBase.security.dao.facade.RoleDao;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RoleSeeder {

    /** Role names used before the 4-role model; renamed in place so user links are kept. */
    private static final Map<String, String> LEGACY_ROLES = Map.of(
            "ADMIN", Roles.SUPER_ADMIN,
            "RESEARCHER", Roles.RESEARCHER
    );

    private final RoleDao roleDao;
    private boolean structureRolesCreated;

    public RoleSeeder(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    /**
     * @return roles keyed by their full name (e.g. {@code ROLE_SUPER_ADMIN})
     */
    public Map<String, Role> seed() {
        migrateLegacyRoles();
        structureRolesCreated = roleDao.findByName(Roles.LAB_DIRECTOR).isEmpty();

        Map<String, Role> roles = new HashMap<>();
        Roles.ALL.forEach(name -> roles.put(name, createIfMissing(name)));
        return roles;
    }

    /**
     * True when LAB_DIRECTOR / TEAM_LEADER did not exist before this run: the seeder then
     * derives them once from the lab structure (directors and team leaders).
     */
    public boolean structureRolesCreated() {
        return structureRolesCreated;
    }

    private void migrateLegacyRoles() {
        LEGACY_ROLES.forEach((legacyName, newName) -> roleDao.findByName(legacyName).ifPresent(legacy -> {
            if (roleDao.findByName(newName).isEmpty()) {
                legacy.setName(newName);
                roleDao.save(legacy);
            }
        }));
    }

    private Role createIfMissing(String name) {
        return roleDao.findByName(name)
                .orElseGet(() -> roleDao.save(new Role(name)));
    }
}
