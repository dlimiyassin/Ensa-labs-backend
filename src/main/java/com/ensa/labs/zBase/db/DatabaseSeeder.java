package com.ensa.labs.zBase.db;

import com.ensa.labs.research.bean.Department;
import com.ensa.labs.research.bean.ResearchField;
import com.ensa.labs.research.dao.DepartmentRepository;
import com.ensa.labs.research.dao.ResearchFieldRepository;
import com.ensa.labs.zBase.security.bean.Role;
import com.ensa.labs.zBase.security.bean.User;
import com.ensa.labs.zBase.security.bean.enums.UserStatus;
import com.ensa.labs.zBase.security.dao.facade.RoleDao;
import com.ensa.labs.zBase.security.dao.facade.UserDao;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final RoleDao roleDao;
    private final UserDao userDao;
    private final DepartmentRepository departmentRepository;
    private final ResearchFieldRepository researchFieldRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(RoleDao roleDao, UserDao userDao, DepartmentRepository departmentRepository, ResearchFieldRepository researchFieldRepository, PasswordEncoder passwordEncoder) {
        this.roleDao = roleDao;
        this.userDao = userDao;
        this.departmentRepository = departmentRepository;
        this.researchFieldRepository = researchFieldRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Role admin = createRoleIfMissing("ADMIN");
        Role labManager = createRoleIfMissing("LAB_MANAGER");
        Role teamLeader = createRoleIfMissing("TEAM_LEADER");
        Role researcher = createRoleIfMissing("RESEARCHER");

        seedUser("admin", "AA111", "admin@ensa.ma", "System", "Admin", Set.of(admin));
        seedUser("khalili.tajeddine", "BK12345", "tajeddine@ensa.ma", "Tajeddine", "Khalili", Set.of(labManager, teamLeader));
        seedUser("researcher.user", "CD77889", "researcher@ensa.ma", "Research", "User", Set.of(researcher));

        departmentRepository.findByName("Computer Science").orElseGet(() -> {
            Department department = new Department();
            department.setName("Computer Science");
            return departmentRepository.save(department);
        });

        researchFieldRepository.findByName("Artificial Intelligence").orElseGet(() -> {
            ResearchField field = new ResearchField();
            field.setName("Artificial Intelligence");
            return researchFieldRepository.save(field);
        });
    }

    private Role createRoleIfMissing(String roleName) {
        return roleDao.findByName(roleName).orElseGet(() -> roleDao.save(new Role(roleName)));
    }

    private void seedUser(String username, String cin, String email, String firstName, String lastName, Set<Role> roles) {
        userDao.findByUsername(username).ifPresentOrElse(existing -> {
            if ((existing.getPassword() == null || existing.getPassword().isBlank()) && existing.getCin() != null) {
                existing.setPassword(passwordEncoder.encode(existing.getCin()));
                userDao.save(existing);
            }
        }, () -> {
            User user = new User();
            user.setUsername(username);
            user.setCin(cin);
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEnabled(true);
            user.setStatus(UserStatus.ACTIF);
            user.setRoles(roles);
            user.setPassword(passwordEncoder.encode(cin));
            userDao.save(user);
        });
    }
}
