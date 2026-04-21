package com.ensa.labs.zBase.db.seeder;

import com.ensa.labs.zBase.db.data.MemberData;
import com.ensa.labs.zBase.security.bean.Role;
import com.ensa.labs.zBase.security.bean.User;
import com.ensa.labs.zBase.security.bean.enums.UserStatus;
import com.ensa.labs.zBase.security.dao.facade.UserDao;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class UserSeeder {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
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
            user.setRoles(Set.of(roles.get("ADMIN")));
            user.setPassword(passwordEncoder.encode("AA111"));
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
            user.setEnabled(true);
            user.setStatus(UserStatus.ACTIF);
            user.setRoles(Set.of(roles.get("RESEARCHER")));
            user.setPassword(passwordEncoder.encode(user.getCin()));
            return userDao.save(user);
        });
    }

    private String generateCin(MemberData data) {
        String base = (data.firstName() + data.lastName()).toUpperCase();
        return base.length() > 8 ? base.substring(0, 8) : (base + "00000000").substring(0, 8);
    }
}
