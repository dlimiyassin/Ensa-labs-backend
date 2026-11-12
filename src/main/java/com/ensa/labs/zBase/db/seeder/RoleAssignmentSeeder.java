package com.ensa.labs.zBase.db.seeder;

import com.ensa.labs.recherche.bean.Equipe;
import com.ensa.labs.recherche.bean.Lab;
import com.ensa.labs.recherche.bean.Member;
import com.ensa.labs.recherche.dao.EquipeRepository;
import com.ensa.labs.zBase.security.bean.Role;
import com.ensa.labs.zBase.security.bean.User;
import com.ensa.labs.zBase.security.constant.Roles;
import com.ensa.labs.zBase.security.dao.facade.UserDao;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Derives the authorization roles from the seeded lab structure:
 * lab directors get ROLE_LAB_DIRECTOR and team responsables get ROLE_TEAM_LEADER.
 * Every seeded member keeps ROLE_RESEARCHER.
 */
@Component
public class RoleAssignmentSeeder {

    private final EquipeRepository equipeRepository;
    private final UserDao userDao;

    public RoleAssignmentSeeder(EquipeRepository equipeRepository, UserDao userDao) {
        this.equipeRepository = equipeRepository;
        this.userDao = userDao;
    }

    public void seed(List<Lab> labs, Map<String, Role> roles) {
        labs.forEach(lab -> grant(lab.getDirecteur(), roles.get(Roles.LAB_DIRECTOR)));
        equipeRepository.findAll().stream()
                .map(Equipe::getResponsable)
                .forEach(responsable -> grant(responsable, roles.get(Roles.TEAM_LEADER)));
    }

    private void grant(Member member, Role role) {
        if (member == null || member.getUser() == null || role == null) {
            return;
        }
        User user = member.getUser();
        if (user.getRoles().stream().noneMatch(r -> role.getName().equals(r.getName()))) {
            var updated = new HashSet<>(user.getRoles());
            updated.add(role);
            user.setRoles(updated);
            userDao.save(user);
        }
    }
}
