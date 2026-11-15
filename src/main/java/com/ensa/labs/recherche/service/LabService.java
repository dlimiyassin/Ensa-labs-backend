package com.ensa.labs.recherche.service;

import com.ensa.labs.exception.GlobalException;
import com.ensa.labs.exception.ResourceAlreadyExistsException;
import com.ensa.labs.exception.ResourceNotFoundException;
import com.ensa.labs.recherche.bean.ComiteGestionMembre;
import com.ensa.labs.recherche.bean.Lab;
import com.ensa.labs.recherche.bean.Member;
import com.ensa.labs.recherche.dao.DepartmentRepository;
import com.ensa.labs.recherche.dao.LabRepository;
import com.ensa.labs.recherche.dao.MemberRepository;
import com.ensa.labs.recherche.dao.PublicationRepository;
import com.ensa.labs.recherche.dao.TagRepository;
import com.ensa.labs.recherche.dao.ThesisRepository;
import com.ensa.labs.recherche.dto.LabDTO;
import com.ensa.labs.recherche.mapper.LabMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class LabService {
    private final LabRepository labRepository;
    private final DepartmentRepository departmentRepository;
    private final TagRepository tagRepository;
    private final MemberRepository memberRepository;
    private final PublicationRepository publicationRepository;
    private final ThesisRepository thesisRepository;
    private final LabMapper labMapper;
    private final ThematiqueRechercheService thematiqueRechercheService;
    private final ProductionService productionService;
    private final AccessControlService accessControlService;

    public LabService(LabRepository labRepository, DepartmentRepository departmentRepository, TagRepository tagRepository,
                      MemberRepository memberRepository, PublicationRepository publicationRepository, ThesisRepository thesisRepository,
                      LabMapper labMapper, ThematiqueRechercheService thematiqueRechercheService, ProductionService productionService,
                      AccessControlService accessControlService) {
        this.labRepository = labRepository;
        this.departmentRepository = departmentRepository;
        this.tagRepository = tagRepository;
        this.memberRepository = memberRepository;
        this.publicationRepository = publicationRepository;
        this.thesisRepository = thesisRepository;
        this.labMapper = labMapper;
        this.thematiqueRechercheService = thematiqueRechercheService;
        this.productionService = productionService;
        this.accessControlService = accessControlService;
    }

    public List<LabDTO> findAll() { return labRepository.findAll().stream().map(labMapper::toDto).toList(); }

    public LabDTO findById(String id) { return labMapper.toDto(get(id)); }

    public LabDTO findByAcronym(String acronym) { return labMapper.toDto(getByAcronym(acronym)); }

    public List<LabDTO> findByEstablishment(String establishment) { return labRepository.findByEstablishmentIgnoreCase(establishment).stream().map(labMapper::toDto).toList(); }

    public List<LabDTO> findByDepartmentName(String departmentName) { return labRepository.findByDepartmentNameIgnoreCase(departmentName).stream().map(labMapper::toDto).toList(); }

    @Transactional
    public LabDTO create(LabDTO dto) {
        assertAcronymAvailable(dto.acronym(), null);
        Lab lab = labMapper.toEntity(dto);
        lab.setId(null);
        applyRelations(lab, dto);
        Lab saved = labRepository.save(lab);
        productionService.getOrCreateForLab(saved);
        return labMapper.toDto(saved);
    }

    @Transactional
    public LabDTO update(String id, LabDTO dto) {
        Lab lab = get(id);
        accessControlService.assertCanManageLab(lab.getAcronym());
        assertAcronymAvailable(dto.acronym(), id);
        lab.setTitleFr(dto.titleFr());
        lab.setTitleEn(dto.titleEn());
        lab.setShortTitle(dto.shortTitle() == null || dto.shortTitle().isBlank() ? null : dto.shortTitle().trim());
        lab.setAcronym(dto.acronym());
        lab.setUniversity(dto.university());
        lab.setProgram(dto.program());
        lab.setAccreditationStart(dto.accreditationStart());
        lab.setAccreditationEnd(dto.accreditationEnd());
        lab.setEstablishment(dto.establishment());
        lab.setPhone(dto.phone());
        lab.setEmail(dto.email());
        applyRelations(lab, dto);
        return labMapper.toDto(labRepository.save(lab));
    }

    /** Only empty labs can be deleted: members, teams and scientific records must be moved or removed first. */
    @Transactional
    public void delete(String id) {
        Lab lab = get(id);
        List<String> blockers = new ArrayList<>();
        if (!lab.getMembers().isEmpty()) blockers.add(lab.getMembers().size() + " member(s)");
        if (!lab.getEquipes().isEmpty()) blockers.add(lab.getEquipes().size() + " team(s)");
        int publications = publicationRepository.findByLabAcronym(lab.getAcronym()).size();
        if (publications > 0) blockers.add(publications + " publication(s)");
        int theses = thesisRepository.findByLabAcronym(lab.getAcronym()).size();
        if (theses > 0) blockers.add(theses + " thesis/theses");
        if (!lab.getCollaborations().isEmpty()) blockers.add(lab.getCollaborations().size() + " collaboration(s)");
        if (!lab.getEquipments().isEmpty()) blockers.add(lab.getEquipments().size() + " equipment(s)");
        if (!lab.getCompetences().isEmpty()) blockers.add(lab.getCompetences().size() + " competence(s)");
        if (!lab.getAxesRecherche().isEmpty()) blockers.add(lab.getAxesRecherche().size() + " research axis/axes");
        if (!blockers.isEmpty()) {
            throw new GlobalException(HttpStatus.CONFLICT, "Lab " + lab.getAcronym() + " is not empty: " + String.join(", ", blockers));
        }
        lab.getDomainesRecherche().forEach(d -> d.getLabs().remove(lab));
        lab.getDomainesRecherche().clear();
        lab.getTags().clear();
        labRepository.delete(lab);
    }

    private void applyRelations(Lab lab, LabDTO dto) {
        if (dto.department() != null && dto.department().id() != null) {
            lab.setDepartment(departmentRepository.findById(dto.department().id()).orElseThrow(() -> new ResourceNotFoundException("Department", "id", dto.department().id())));
        }
        if (dto.thematiquesRecherche() != null) {
            lab.getDomainesRecherche().forEach(d -> d.getLabs().remove(lab));
            var thematiques = new HashSet<>(dto.thematiquesRecherche().stream()
                    .map(thematiqueRechercheService::resolve)
                    .toList());
            thematiques.forEach(d -> d.getLabs().add(lab));
            lab.setDomainesRecherche(thematiques);
        }
        if (dto.tags() != null) {
            lab.setTags(new HashSet<>(dto.tags().stream()
                    .filter(t -> t.id() != null)
                    .map(t -> tagRepository.findById(t.id()).orElseThrow(() -> new ResourceNotFoundException("Tag", "id", t.id())))
                    .toList()));
        }
        lab.setDirecteur(findMember(dto.directeur() == null ? null : dto.directeur().id()));
        lab.setDirecteurAdjoint(findMember(dto.directeurAdjoint() == null ? null : dto.directeurAdjoint().id()));
        if (dto.comiteGestion() != null) {
            lab.setComiteGestion(new ArrayList<>(dto.comiteGestion().stream().map(c -> {
                ComiteGestionMembre membre = new ComiteGestionMembre();
                Member member = findMember(c.memberId());
                membre.setMember(member);
                String name = c.nomEnseignant();
                if ((name == null || name.isBlank()) && member != null) {
                    name = member.getFirstName() + " " + member.getLastName();
                }
                membre.setNomEnseignant(name);
                membre.setRoleComite(c.roleComite());
                return membre;
            }).toList()));
        }
    }

    private Member findMember(String id) {
        if (id == null || id.isBlank()) return null;
        return memberRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Member", "id", id));
    }

    private void assertAcronymAvailable(String acronym, String currentId) {
        labRepository.findByAcronym(acronym)
                .filter(other -> !other.getId().equals(currentId))
                .ifPresent(other -> {
                    throw new ResourceAlreadyExistsException("Lab", "acronym", acronym);
                });
    }

    private Lab get(String id) { return labRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lab", "id", id)); }

    private Lab getByAcronym(String acronym) { return labRepository.findByAcronym(acronym).orElseThrow(() -> new ResourceNotFoundException("Lab", "acronym", acronym)); }
}
