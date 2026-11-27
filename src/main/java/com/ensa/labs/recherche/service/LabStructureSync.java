package com.ensa.labs.recherche.service;

import com.ensa.labs.recherche.bean.ComiteGestionMembre;
import com.ensa.labs.recherche.bean.Lab;
import com.ensa.labs.recherche.bean.Member;
import com.ensa.labs.recherche.bean.enums.MemberRoleInLab;
import com.ensa.labs.recherche.dao.LabRepository;
import com.ensa.labs.zBase.security.bean.Role;
import com.ensa.labs.zBase.security.bean.User;
import com.ensa.labs.zBase.security.constant.Roles;
import com.ensa.labs.zBase.security.dao.facade.RoleDao;
import com.ensa.labs.zBase.security.dao.facade.UserDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Keeps what the lab structure says in line with what it grants:
 * <ul>
 *   <li>the director and the deputy director of a lab both get ROLE_LAB_DIRECTOR;</li>
 *   <li>a member whose role in the lab is "management committee" gets a seat in the lab's committee,
 *       which is what the public lab page shows.</li>
 * </ul>
 */
@Service
public class LabStructureSync {

    /** Committee role given to a seat created from the member form. */
    public static final String DEFAULT_COMMITTEE_ROLE = "Membre du comité de gestion";

    private final LabRepository labRepository;
    private final UserDao userDao;
    private final RoleDao roleDao;

    public LabStructureSync(LabRepository labRepository, UserDao userDao, RoleDao roleDao) {
        this.labRepository = labRepository;
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    /**
     * Startup pass over data saved before this sync existed: directors and deputies get their role,
     * and "management committee" members without any committee seat get one.
     */
    @Transactional
    public void syncAll() {
        syncDirectorRoles(List.of());
        // Collected first: syncing a seat saves the lab, which must not happen while its members are iterated.
        List<Member> committeeMembers = labRepository.findAll().stream()
                .flatMap(lab -> List.copyOf(lab.getMembers()).stream())
                .filter(member -> member.getRoleInLab() == MemberRoleInLab.MANAGEMENT_COMMITTEE)
                .toList();
        committeeMembers.forEach(this::syncCommitteeSeat);
    }

    /**
     * Grants ROLE_LAB_DIRECTOR to the director and deputy director of every lab, and takes it back
     * from the given former director / deputy members when they no longer lead any lab.
     */
    @Transactional
    public void syncDirectorRoles(Collection<Member> formerLeaders) {
        Role role = roleDao.findByName(Roles.LAB_DIRECTOR).orElse(null);
        if (role == null) {
            return;
        }
        Set<String> leaderUserIds = new HashSet<>();
        for (Lab lab : labRepository.findAll()) {
            for (Member leader : Arrays.asList(lab.getDirecteur(), lab.getDirecteurAdjoint())) {
                if (leader != null && leader.getUser() != null) {
                    leaderUserIds.add(leader.getUser().getId());
                    grant(leader.getUser(), role);
                }
            }
        }
        for (Member former : formerLeaders) {
            if (former != null && former.getUser() != null && !leaderUserIds.contains(former.getUser().getId())) {
                revoke(former.getUser(), role);
            }
        }
    }

    /**
     * Gives the member a committee seat in its lab when its role in the lab is "management committee".
     * When it is not (any more), only the seats created here are removed: seats with a real
     * responsibility set in the lab form are kept.
     */
    @Transactional
    public void syncCommitteeSeat(Member member) {
        if (member == null || member.getId() == null) {
            return;
        }
        for (Lab lab : labRepository.findAll()) {
            boolean ownLab = member.getLab() != null && Objects.equals(lab.getId(), member.getLab().getId());
            boolean wantsSeat = ownLab && member.getRoleInLab() == MemberRoleInLab.MANAGEMENT_COMMITTEE;
            boolean hasSeat = lab.getComiteGestion().stream().anyMatch(seat -> isSeatOf(seat, member));
            boolean changed = false;
            if (wantsSeat && !hasSeat) {
                ComiteGestionMembre seat = new ComiteGestionMembre();
                seat.setMember(member);
                seat.setNomEnseignant(member.getFirstName() + " " + member.getLastName());
                seat.setRoleComite(DEFAULT_COMMITTEE_ROLE);
                lab.getComiteGestion().add(seat);
                changed = true;
            } else if (!wantsSeat) {
                changed = lab.getComiteGestion().removeIf(seat ->
                        isSeatOf(seat, member) && DEFAULT_COMMITTEE_ROLE.equals(seat.getRoleComite()));
            }
            if (changed) {
                labRepository.save(lab);
            }
        }
    }

    private static boolean isSeatOf(ComiteGestionMembre seat, Member member) {
        return seat.getMember() != null && Objects.equals(seat.getMember().getId(), member.getId());
    }

    private void grant(User user, Role role) {
        if (user.getRoles().stream().noneMatch(r -> role.getName().equals(r.getName()))) {
            Set<Role> updated = new HashSet<>(user.getRoles());
            updated.add(role);
            user.setRoles(updated);
            userDao.save(user);
        }
    }

    private void revoke(User user, Role role) {
        Set<Role> updated = new HashSet<>(user.getRoles());
        if (updated.removeIf(r -> role.getName().equals(r.getName()))) {
            user.setRoles(updated);
            userDao.save(user);
        }
    }
}
