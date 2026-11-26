package com.ensa.labs.zBase.db.seeder;

import com.ensa.labs.zBase.db.data.MemberData;
import com.ensa.labs.zBase.security.bean.Role;
import com.ensa.labs.zBase.security.bean.User;
import com.ensa.labs.zBase.security.bean.enums.UserStatus;
import com.ensa.labs.zBase.security.dao.facade.UserDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ensa.labs.zBase.security.constant.Roles;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Component
public class UserSeeder {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final String adminPassword;

    public UserSeeder(UserDao userDao, PasswordEncoder passwordEncoder,
                      @Value("${ENSA_ADMIN_PASSWORD:}") String adminPassword) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.adminPassword = adminPassword;
    }

    public User seedAdmin(Map<String, Role> roles) {
        return userDao.findByUsername("admin").orElseGet(() -> {
            User user = new User();
            user.setUsername("admin");
            user.setCin("AA111");
            user.setEmail("admin@ensa.ma");
            user.setFirstName("System");
            user.setLastName("Admin");
            user.setEnabled(true);
            user.setStatus(UserStatus.ACTIF);
            user.setRoles(new HashSet<>(Set.of(roles.get(Roles.SUPER_ADMIN))));
            if (adminPassword.length() < 20) {
                throw new IllegalStateException("ENSA_ADMIN_PASSWORD must have at least 20 characters");
            }
            user.setPassword(passwordEncoder.encode(adminPassword));
            return userDao.save(user);
        });
    }

    public User createResearcher(MemberData data, Map<String, Role> roles) {
        String username = (data.firstName() + "." + data.lastName()).toLowerCase();

        return userDao.findByUsername(username).orElseGet(() -> {
            User user = new User();
            user.setUsername(username);
            user.setCin(generateCin(data));
            user.setFirstName(data.firstName());
            user.setLastName(data.lastName());
            user.setEnabled(false);
            user.setStatus(UserStatus.EN_ATTENTE);
            user.setRoles(new HashSet<>(Set.of(roles.get(Roles.RESEARCHER))));
            user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            return userDao.save(user);
        });
    }

    private String generateCin(MemberData data) {
        String base = (data.firstName() + data.lastName()).toUpperCase();
        return base.length() > 8 ? base.substring(0, 8) : (base + "00000000").substring(0, 8);
    }
}
