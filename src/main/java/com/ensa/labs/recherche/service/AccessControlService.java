package com.ensa.labs.recherche.service;

import com.ensa.labs.recherche.bean.Equipe;
import com.ensa.labs.recherche.bean.Lab;
import com.ensa.labs.recherche.bean.Member;
import com.ensa.labs.recherche.bean.Publication;
import com.ensa.labs.recherche.dao.EquipeRepository;
import com.ensa.labs.recherche.dao.MemberRepository;
import com.ensa.labs.recherche.dao.LabRepository;
import com.ensa.labs.zBase.security.bean.User;
import com.ensa.labs.zBase.security.constant.Roles;
import com.ensa.labs.zBase.security.dao.facade.UserDao;
import com.ensa.labs.zBase.testing.TestProfileAccess;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Data-ownership rules (lab / team scope). Role checks are done with {@code @PreAuthorize}
 * on the controllers; this service enforces that a LAB_DIRECTOR only touches his lab,
 * a TEAM_LEADER only his teams and a RESEARCHER only his own publications.
 */
@Service
public class AccessControlService {

    private final UserDao userDao;
    private final MemberRepository memberRepository;
    private final EquipeRepository equipeRepository;
    private final LabRepository labRepository;
    private final boolean securityEnabled;

    public AccessControlService(UserDao userDao,
                                MemberRepository memberRepository,
                                EquipeRepository equipeRepository,
                                LabRepository labRepository,
                                @Value("${security.enabled:true}") boolean securityEnabled) {
        this.userDao = userDao;
        this.memberRepository = memberRepository;
        this.equipeRepository = equipeRepository;
        this.labRepository = labRepository;
        this.securityEnabled = securityEnabled;
    }

    /** Snapshot of the caller: roles + lab/team scope. */
    public record Scope(User user, Set<String> roles, Member member, String labAcronym, Set<String> ledEquipeIds) {
        public boolean has(String role) {
            return roles.contains(role);
        }

        public boolean isTestAccount() {
            return user != null && TestProfileAccess.isTestAccount(user.getUsername());
        }

        public boolean isSuperAdmin() {
            return has(Roles.SUPER_ADMIN);
        }

        public boolean inLab(String acronym) {
            return labAcronym != null && acronym != null && labAcronym.equalsIgnoreCase(acronym);
        }

        public boolean leads(String equipeId) {
            return equipeId != null && ledEquipeIds.contains(equipeId);
        }
    }

    public Scope currentScope() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken || !authentication.isAuthenticated()) {
            if (!securityEnabled) {
                return new Scope(null, Set.of(Roles.SUPER_ADMIN), null, null, Set.of());
            }
            throw new AccessDeniedException("Authentication required");
        }
        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        User user = userDao.findByUsername(authentication.getName()).orElse(null);
        if (user != null && TestProfileAccess.isTestAccount(user.getUsername())) {
            return testScope(user, roles);
        }
        Member member = user == null ? null : memberRepository.findByUserId(user.getId()).orElse(null);
        String labAcronym = member != null && member.getLab() != null ? member.getLab().getAcronym() : null;
        Set<String> ledEquipeIds = member == null ? Set.of() : equipeRepository.findByResponsableId(member.getId()).stream()
                .map(Equipe::getId)
                .collect(Collectors.toSet());
        return new Scope(user, roles, member, labAcronym, ledEquipeIds);
    }

    private Scope testScope(User user, Set<String> roles) {
        if (roles.contains(Roles.SUPER_ADMIN)) {
            return new Scope(user, roles, null, null, Set.of());
        }
        List<Lab> labs = labRepository.findAll(Sort.by("acronym"));
        if (labs.isEmpty()) {
            return new Scope(user, roles, null, null, Set.of());
        }
        int index = labs.size() > 1 && user.getUsername().endsWith("-b") ? 1 : 0;
        String acronym = labs.get(index).getAcronym();
        Set<String> teamIds = roles.contains(Roles.TEAM_LEADER)
                ? equipeRepository.findByLabAcronym(acronym).stream().min(Comparator.comparing(Equipe::getId))
                        .map(team -> Set.of(team.getId())).orElse(Set.of())
                : Set.of();
        return new Scope(user, roles, null, acronym, teamIds);
    }

    public Optional<User> currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        return userDao.findByUsername(authentication.getName());
    }

    /** Structure of a lab (members, teams, collaborations, resources...): SUPER_ADMIN or the lab's director. */
    public void assertCanManageLab(String labAcronym) {
        Scope scope = currentScope();
        if (scope.isSuperAdmin()) return;
        if (scope.has(Roles.LAB_DIRECTOR) && scope.inLab(labAcronym)) return;
        throw new AccessDeniedException("You can only manage your own lab");
    }

    /** Scientific production of a lab (productions, theses): director or team leader of that lab. */
    public void assertCanEditLabProduction(String labAcronym) {
        Scope scope = currentScope();
        if (scope.isSuperAdmin()) return;
        if ((scope.has(Roles.LAB_DIRECTOR) || scope.has(Roles.TEAM_LEADER)) && scope.inLab(labAcronym)) return;
        throw new AccessDeniedException("You can only edit the production of your own lab");
    }

    public void assertCanCreatePublication(String labAcronym, String equipeId) {
        Scope scope = currentScope();
        if (scope.isSuperAdmin()) return;
        if (scope.has(Roles.LAB_DIRECTOR) && scope.inLab(labAcronym)) return;
        if (scope.has(Roles.TEAM_LEADER) && scope.inLab(labAcronym) && scope.leads(equipeId)) return;
        if (scope.has(Roles.RESEARCHER) && scope.inLab(labAcronym)) return;
        throw new AccessDeniedException("You cannot add a publication to this lab or team");
    }

    public void assertCanUpdatePublication(Publication existing, String newLabAcronym, String newEquipeId) {
        Scope scope = currentScope();
        if (scope.isSuperAdmin()) return;
        String currentLab = existing.getLab() != null ? existing.getLab().getAcronym() : null;
        String currentEquipe = existing.getEquipe() != null ? existing.getEquipe().getId() : null;
        if (scope.has(Roles.LAB_DIRECTOR) && scope.inLab(currentLab) && scope.inLab(newLabAcronym)) return;
        if (scope.has(Roles.TEAM_LEADER) && scope.leads(currentEquipe) && scope.leads(newEquipeId)) return;
        if (scope.has(Roles.RESEARCHER) && isOwner(scope, existing) && scope.inLab(newLabAcronym)) return;
        throw new AccessDeniedException("You can only update publications you are responsible for");
    }

    public void assertCanDeletePublication(Publication existing) {
        assertCanManageLab(existing.getLab() != null ? existing.getLab().getAcronym() : null);
    }

    /**
     * Who may approve / reject a publication of this lab and team: the super admin, the lab's
     * director, or the leader of the publication's team. Their own records are validated directly.
     */
    public boolean canReviewPublication(Scope scope, String labAcronym, String equipeId) {
        if (scope.isSuperAdmin()) return true;
        if (scope.has(Roles.LAB_DIRECTOR) && scope.inLab(labAcronym)) return true;
        return scope.has(Roles.TEAM_LEADER) && scope.inLab(labAcronym) && scope.leads(equipeId);
    }

    public void assertCanReviewPublication(Scope scope, Publication publication) {
        if (!canReviewPublication(scope, labOf(publication), equipeOf(publication))) {
            throw new AccessDeniedException("Only the lab director or the team leader can review this publication");
        }
    }

    /**
     * Dashboard visibility: directors and team leaders see every record of their lab,
     * researchers see the validated records of their lab plus their own submissions.
     */
    public boolean canSeeInDashboard(Scope scope, Publication publication) {
        if (scope.isTestAccount() && !publication.isPublic()) return false;
        if (scope.isSuperAdmin() || isOwner(scope, publication)) return true;
        if (!scope.inLab(labOf(publication))) return false;
        if (scope.has(Roles.LAB_DIRECTOR) || scope.has(Roles.TEAM_LEADER)) return true;
        return publication.isPublic();
    }

    public boolean isOwner(Scope scope, Publication publication) {
        return scope.user() != null && publication.getCreatedBy() != null
                && Objects.equals(scope.user().getId(), publication.getCreatedBy().getId());
    }

    private static String labOf(Publication publication) {
        return publication.getLab() != null ? publication.getLab().getAcronym() : null;
    }

    private static String equipeOf(Publication publication) {
        return publication.getEquipe() != null ? publication.getEquipe().getId() : null;
    }
}
