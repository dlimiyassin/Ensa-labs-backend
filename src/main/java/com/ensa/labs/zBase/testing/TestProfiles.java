package com.ensa.labs.zBase.testing;

import com.ensa.labs.zBase.db.seeder.RoleSeeder;
import com.ensa.labs.zBase.security.bean.Role;
import com.ensa.labs.zBase.security.bean.User;
import com.ensa.labs.zBase.security.bean.enums.UserStatus;
import com.ensa.labs.zBase.security.constant.Roles;
import com.ensa.labs.zBase.security.dao.facade.UserDao;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class TestProfiles implements ApplicationRunner {
    private final TestProfileAccess access;
    private final RoleSeeder roles;
    private final UserDao users;
    private final PasswordEncoder passwords;
    private final TransactionTemplate transaction;
    private volatile Map<TestRole, List<Credentials>> credentials = Map.of();

    public TestProfiles(TestProfileAccess access, RoleSeeder roles, UserDao users, PasswordEncoder passwords,
                        PlatformTransactionManager transactionManager) {
        this.access = access;
        this.roles = roles;
        this.users = users;
        this.passwords = passwords;
        this.transaction = new TransactionTemplate(transactionManager);
    }

    @Override
    public void run(ApplicationArguments args) {
        if (access.isEnabled()) {
            credentials = transaction.execute(status -> {
                Map<String, Role> assignedRoles = roles.seed();
                return Arrays.stream(TestRole.values()).collect(Collectors.toUnmodifiableMap(
                        role -> role,
                        role -> List.of(prepareAccount(role, "A", assignedRoles), prepareAccount(role, "B", assignedRoles))));
            });
        }
    }

    List<ProfileOption> options() {
        return Arrays.stream(TestRole.values())
                .filter(credentials::containsKey)
                .map(role -> new ProfileOption(role.id, role.label))
                .toList();
    }

    Optional<Credentials> randomCredentials(String id) {
        return Arrays.stream(TestRole.values())
                .filter(role -> role.id.equals(id))
                .map(role -> credentials.getOrDefault(role, List.of()))
                .filter(accounts -> !accounts.isEmpty())
                .map(accounts -> accounts.get(ThreadLocalRandom.current().nextInt(accounts.size())))
                .findFirst();
    }

    private Credentials prepareAccount(TestRole profile, String group, Map<String, Role> assignedRoles) {
        String username = "ensa-test-" + profile.id + "-" + group.toLowerCase(Locale.ROOT);
        String email = username + "@ensa-labs.test";
        User account = users.findByUsername(username).orElseGet(User::new);
        if (account.getId() != null && !email.equals(account.getEmail())) {
            throw new IllegalStateException("A reserved test account name is already in use.");
        }
        String password = UUID.randomUUID().toString();
        account.setUsername(username);
        account.setEmail(email);
        account.setFirstName("Compte test " + group);
        account.setLastName(profile.label);
        account.setRoles(new HashSet<>(Set.of(assignedRoles.get(profile.authority))));
        account.setPassword(passwords.encode(password));
        account.setEnabled(true);
        account.setStatus(UserStatus.ACTIF);
        users.save(account);
        return new Credentials(email, password);
    }

    record ProfileOption(String id, String label) {}
    record Credentials(String email, String password) {}

    private enum TestRole {
        ADMIN("super-admin", "Super administrateur", Roles.SUPER_ADMIN),
        DIRECTOR("lab-director", "Directeur de laboratoire", Roles.LAB_DIRECTOR),
        LEADER("team-leader", "Responsable d’équipe", Roles.TEAM_LEADER),
        RESEARCHER("researcher", "Chercheur", Roles.RESEARCHER);

        private final String id;
        private final String label;
        private final String authority;

        TestRole(String id, String label, String authority) {
            this.id = id;
            this.label = label;
            this.authority = authority;
        }
    }
}
