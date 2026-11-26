package com.ensa.labs.recherche.service;

import com.ensa.labs.recherche.bean.Equipe;
import com.ensa.labs.recherche.bean.Lab;
import com.ensa.labs.recherche.bean.Member;
import com.ensa.labs.recherche.bean.Publication;
import com.ensa.labs.recherche.bean.enums.PublicationStatus;
import com.ensa.labs.recherche.dao.EquipeRepository;
import com.ensa.labs.recherche.dao.MemberRepository;
import com.ensa.labs.recherche.dao.LabRepository;
import com.ensa.labs.zBase.security.bean.User;
import com.ensa.labs.zBase.security.constant.Roles;
import com.ensa.labs.zBase.security.dao.facade.UserDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** Lab / team ownership rules enforced on top of the role checks. */
class AccessControlServiceTest {

    private final UserDao userDao = mock(UserDao.class);
    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final EquipeRepository equipeRepository = mock(EquipeRepository.class);
    private final LabRepository labRepository = mock(LabRepository.class);
    private final AccessControlService service = new AccessControlService(userDao, memberRepository, equipeRepository, labRepository, true);

    private Lab laresi;
    private Lab lrsta;
    private Equipe laresiTeam;

    @BeforeEach
    void setUp() {
        laresi = lab("LaRESI");
        lrsta = lab("LRSTA");
        laresiTeam = new Equipe();
        laresiTeam.setId("team-1");
        laresiTeam.setLab(laresi);
        when(equipeRepository.findByResponsableId(anyString())).thenReturn(List.of());
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void superAdminManagesEveryLab() {
        signIn("admin", null, Roles.SUPER_ADMIN);

        assertThatCode(() -> service.assertCanManageLab("LRSTA")).doesNotThrowAnyException();
        assertThatCode(() -> service.assertCanManageLab("LaRESI")).doesNotThrowAnyException();
    }

    @Test
    void labDirectorOnlyManagesHisLab() {
        signIn("director", laresi, Roles.LAB_DIRECTOR, Roles.RESEARCHER);

        assertThatCode(() -> service.assertCanManageLab("LaRESI")).doesNotThrowAnyException();
        assertThatThrownBy(() -> service.assertCanManageLab("LRSTA")).isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void researcherCannotManageLabStructure() {
        signIn("researcher", laresi, Roles.RESEARCHER);

        assertThatThrownBy(() -> service.assertCanManageLab("LaRESI")).isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void teamLeaderPublishesOnlyForHisTeam() {
        Member leader = signIn("leader", laresi, Roles.TEAM_LEADER);
        when(equipeRepository.findByResponsableId(leader.getId())).thenReturn(List.of(laresiTeam));

        assertThatCode(() -> service.assertCanCreatePublication("LaRESI", "team-1")).doesNotThrowAnyException();
        assertThatThrownBy(() -> service.assertCanCreatePublication("LaRESI", "other-team")).isInstanceOf(AccessDeniedException.class);
        assertThatThrownBy(() -> service.assertCanCreatePublication("LRSTA", "team-1")).isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void secondTestLeaderUsesAnExistingTeamWithoutASeparateLab() {
        signIn("ensa-test-team-leader-b", null, Roles.TEAM_LEADER);
        when(equipeRepository.findAll(Sort.by("id"))).thenReturn(List.of(laresiTeam));

        var scope = service.currentScope();
        assertThat(scope.labAcronym()).isEqualTo("LaRESI");
        assertThat(scope.ledEquipeIds()).containsExactly("team-1");
        assertThat(scope.member()).isNull();
    }

    @Test
    void researcherUpdatesOnlyHisOwnPublications() {
        signIn("researcher", laresi, Roles.RESEARCHER);
        User author = userDao.findByUsername("researcher").orElseThrow();
        User someoneElse = new User();
        someoneElse.setId("someone-else");

        Publication own = publication(laresi, author);
        Publication foreign = publication(laresi, someoneElse);

        assertThatCode(() -> service.assertCanCreatePublication("LaRESI", null)).doesNotThrowAnyException();
        assertThatCode(() -> service.assertCanUpdatePublication(own, "LaRESI", null)).doesNotThrowAnyException();
        assertThatThrownBy(() -> service.assertCanUpdatePublication(own, "LRSTA", null)).isInstanceOf(AccessDeniedException.class);
        assertThatThrownBy(() -> service.assertCanUpdatePublication(foreign, "LaRESI", null)).isInstanceOf(AccessDeniedException.class);
        assertThatThrownBy(() -> service.assertCanDeletePublication(own)).isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void directorReviewsHisLabAndLeaderReviewsHisTeams() {
        signIn("director", laresi, Roles.LAB_DIRECTOR, Roles.RESEARCHER);
        var director = service.currentScope();
        assertThat(service.canReviewPublication(director, "LaRESI", null)).isTrue();
        assertThat(service.canReviewPublication(director, "LRSTA", null)).isFalse();

        Member leader = signIn("leader", laresi, Roles.TEAM_LEADER, Roles.RESEARCHER);
        when(equipeRepository.findByResponsableId(leader.getId())).thenReturn(List.of(laresiTeam));
        var teamLeader = service.currentScope();
        assertThat(service.canReviewPublication(teamLeader, "LaRESI", "team-1")).isTrue();
        assertThat(service.canReviewPublication(teamLeader, "LaRESI", null)).isFalse();
        assertThat(service.canReviewPublication(teamLeader, "LaRESI", "other-team")).isFalse();
    }

    @Test
    void researcherCannotReviewAndOnlySeesValidatedOrOwnRecords() {
        signIn("researcher", laresi, Roles.RESEARCHER);
        var scope = service.currentScope();
        User author = userDao.findByUsername("researcher").orElseThrow();
        User someoneElse = new User();
        someoneElse.setId("someone-else");

        Publication ownPending = publication(laresi, author);
        ownPending.setStatus(PublicationStatus.PENDING);
        Publication foreignPending = publication(laresi, someoneElse);
        foreignPending.setStatus(PublicationStatus.PENDING);
        Publication foreignValidated = publication(laresi, someoneElse);
        foreignValidated.setStatus(PublicationStatus.VALIDATED);

        assertThat(service.canReviewPublication(scope, "LaRESI", null)).isFalse();
        assertThatThrownBy(() -> service.assertCanReviewPublication(scope, ownPending)).isInstanceOf(AccessDeniedException.class);
        assertThat(service.canSeeInDashboard(scope, ownPending)).isTrue();
        assertThat(service.canSeeInDashboard(scope, foreignValidated)).isTrue();
        assertThat(service.canSeeInDashboard(scope, foreignPending)).isFalse();
    }

    @Test
    void anonymousCallerIsDenied() {
        assertThatThrownBy(() -> service.assertCanManageLab("LaRESI")).isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void everythingAllowedWhenSecurityIsDisabled() {
        AccessControlService open = new AccessControlService(userDao, memberRepository, equipeRepository, labRepository, false);

        assertThatCode(() -> open.assertCanManageLab("LRSTA")).doesNotThrowAnyException();
    }

    private Member signIn(String username, Lab lab, String... roles) {
        User user = new User();
        user.setId("user-" + username);
        user.setUsername(username);
        when(userDao.findByUsername(username)).thenReturn(Optional.of(user));

        Member member = null;
        if (lab != null) {
            member = new Member();
            member.setId("member-" + username);
            member.setLab(lab);
            member.setUser(user);
        }
        when(memberRepository.findByUserId(user.getId())).thenReturn(Optional.ofNullable(member));

        var authorities = Arrays.stream(roles).map(SimpleGrantedAuthority::new).toList();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, null, authorities));
        return member;
    }

    private static Lab lab(String acronym) {
        Lab lab = new Lab();
        lab.setId("lab-" + acronym);
        lab.setAcronym(acronym);
        return lab;
    }

    private static Publication publication(Lab lab, User createdBy) {
        Publication publication = new Publication();
        publication.setId("pub-" + createdBy.getId());
        publication.setLab(lab);
        publication.setCreatedBy(createdBy);
        return publication;
    }
}
