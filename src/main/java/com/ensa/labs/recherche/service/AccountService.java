package com.ensa.labs.recherche.service;

import com.ensa.labs.exception.GlobalException;
import com.ensa.labs.recherche.bean.enums.PublicationType;
import com.ensa.labs.recherche.dao.CollaborationRepository;
import com.ensa.labs.recherche.dao.CompetenceRepository;
import com.ensa.labs.recherche.dao.EquipeRepository;
import com.ensa.labs.recherche.dao.EquipmentRepository;
import com.ensa.labs.recherche.dao.LabRepository;
import com.ensa.labs.recherche.dao.MemberRepository;
import com.ensa.labs.recherche.dao.PublicationRepository;
import com.ensa.labs.recherche.dao.ThesisRepository;
import com.ensa.labs.recherche.dto.CurrentUserDTO;
import com.ensa.labs.recherche.dto.DashboardStatsDTO;
import com.ensa.labs.recherche.dto.PasswordChangeDTO;
import com.ensa.labs.zBase.security.bean.User;
import com.ensa.labs.zBase.security.dao.facade.UserDao;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Predicate;

/** Endpoints about the authenticated account itself (profile, password, dashboard counters). */
@Service
public class AccountService {
    private final AccessControlService accessControlService;
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final LabRepository labRepository;
    private final MemberRepository memberRepository;
    private final EquipeRepository equipeRepository;
    private final PublicationRepository publicationRepository;
    private final ThesisRepository thesisRepository;
    private final CollaborationRepository collaborationRepository;
    private final EquipmentRepository equipmentRepository;
    private final CompetenceRepository competenceRepository;

    public AccountService(AccessControlService accessControlService, UserDao userDao, PasswordEncoder passwordEncoder,
                          LabRepository labRepository, MemberRepository memberRepository, EquipeRepository equipeRepository,
                          PublicationRepository publicationRepository, ThesisRepository thesisRepository,
                          CollaborationRepository collaborationRepository, EquipmentRepository equipmentRepository,
                          CompetenceRepository competenceRepository) {
        this.accessControlService = accessControlService;
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.labRepository = labRepository;
        this.memberRepository = memberRepository;
        this.equipeRepository = equipeRepository;
        this.publicationRepository = publicationRepository;
        this.thesisRepository = thesisRepository;
        this.collaborationRepository = collaborationRepository;
        this.equipmentRepository = equipmentRepository;
        this.competenceRepository = competenceRepository;
    }

    @Transactional(readOnly = true)
    public CurrentUserDTO me() {
        AccessControlService.Scope scope = accessControlService.currentScope();
        User user = scope.user();
        if (user == null) {
            throw new GlobalException(HttpStatus.UNAUTHORIZED, "Unknown account");
        }
        return new CurrentUserDTO(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                scope.roles().stream().sorted().toList(),
                scope.member() != null ? scope.member().getId() : null,
                scope.labAcronym(),
                scope.ledEquipeIds().stream().sorted().toList(),
                scope.member() != null ? scope.member().getImage() : null);
    }

    @Transactional
    public void changePassword(PasswordChangeDTO dto) {
        User user = accessControlService.currentUser()
                .orElseThrow(() -> new GlobalException(HttpStatus.UNAUTHORIZED, "Unknown account"));
        if (!passwordEncoder.matches(dto.currentPassword(), user.getPassword())) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        userDao.save(user);
    }

    /** Global counters for a super admin, lab counters for everybody else (or for the requested lab). */
    @Transactional(readOnly = true)
    public DashboardStatsDTO stats(String requestedLab) {
        AccessControlService.Scope scope = accessControlService.currentScope();
        String lab = requestedLab != null && !requestedLab.isBlank() ? requestedLab
                : scope.isSuperAdmin() ? null : scope.labAcronym();
        if (lab == null) {
            var publications = publicationRepository.findAll();
            return new DashboardStatsDTO(null,
                    labRepository.count(),
                    memberRepository.count(),
                    equipeRepository.count(),
                    publications.stream().filter(isCommunication().negate()).count(),
                    publications.stream().filter(isCommunication()).count(),
                    thesisRepository.count(),
                    collaborationRepository.count(),
                    equipmentRepository.count(),
                    competenceRepository.count());
        }
        var publications = publicationRepository.findByLabAcronym(lab);
        return new DashboardStatsDTO(lab,
                labRepository.findByAcronym(lab).isPresent() ? 1 : 0,
                memberRepository.findByLabAcronym(lab).size(),
                equipeRepository.findByLabAcronym(lab).size(),
                publications.stream().filter(isCommunication().negate()).count(),
                publications.stream().filter(isCommunication()).count(),
                thesisRepository.findByLabAcronym(lab).size(),
                collaborationRepository.findByLabAcronym(lab).size(),
                equipmentRepository.findByLabAcronym(lab).size(),
                competenceRepository.findByLabAcronym(lab).size());
    }

    private static Predicate<com.ensa.labs.recherche.bean.Publication> isCommunication() {
        return p -> p.getType() == PublicationType.COMMUNICATION;
    }
}
