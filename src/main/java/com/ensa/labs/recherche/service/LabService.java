package com.ensa.labs.recherche.service;

import com.ensa.labs.exception.ResourceNotFoundException;
import com.ensa.labs.recherche.bean.Lab;
import com.ensa.labs.recherche.dao.*;
import com.ensa.labs.recherche.dto.LabDTO;
import com.ensa.labs.recherche.mapper.LabMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class LabService {
    private final LabRepository labRepository;
    private final DepartmentRepository departmentRepository;
    private final DomaineRechercheRepository domaineRechercheRepository;
    private final TagRepository tagRepository;
    private final MemberRepository memberRepository;
    private final LabMapper labMapper;

    public LabService(LabRepository labRepository, DepartmentRepository departmentRepository, DomaineRechercheRepository domaineRechercheRepository, TagRepository tagRepository, MemberRepository memberRepository, LabMapper labMapper) {
        this.labRepository = labRepository;
        this.departmentRepository = departmentRepository;
        this.domaineRechercheRepository = domaineRechercheRepository;
        this.tagRepository = tagRepository;
        this.memberRepository = memberRepository;
        this.labMapper = labMapper;
    }

    public List<LabDTO> findAll() { return labRepository.findAll().stream().map(labMapper::toDto).toList(); }
    public LabDTO findById(String id) { return labMapper.toDto(get(id)); }
    public LabDTO findByAcronym(String acronym) { return labMapper.toDto(getByAcronym(acronym)); }
    public List<LabDTO> findByEstablishment(String establishment) { return labRepository.findByEstablishmentIgnoreCase(establishment).stream().map(labMapper::toDto).toList(); }
    public List<LabDTO> findByDepartmentName(String departmentName) { return labRepository.findByDepartmentNameIgnoreCase(departmentName).stream().map(labMapper::toDto).toList(); }

    public LabDTO create(LabDTO dto) {
        Lab lab = labMapper.toEntity(dto);
        applyRelations(lab, dto);
        return labMapper.toDto(labRepository.save(lab));
    }

    public LabDTO update(String id, LabDTO dto) {
        Lab lab = get(id);
        lab.setTitleFr(dto.titleFr());
        lab.setTitleEn(dto.titleEn());
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

    public void delete(String id) { labRepository.delete(get(id)); }

    private void applyRelations(Lab lab, LabDTO dto) {
        if (dto.department() != null && dto.department().id() != null) {
            lab.setDepartment(departmentRepository.findById(dto.department().id()).orElseThrow(() -> new ResourceNotFoundException("Department", "id", dto.department().id())));
        }
        if (dto.domainesRecherche() != null) {
            lab.setDomainesRecherche(new HashSet<>(dto.domainesRecherche().stream()
                    .filter(d -> d.id() != null)
                    .map(d -> domaineRechercheRepository.findById(d.id()).orElseThrow(() -> new ResourceNotFoundException("DomaineRecherche", "id", d.id())))
                    .toList()));
        }
        if (dto.tags() != null) {
            lab.setTags(new HashSet<>(dto.tags().stream()
                    .filter(t -> t.id() != null)
                    .map(t -> tagRepository.findById(t.id()).orElseThrow(() -> new ResourceNotFoundException("Tag", "id", t.id())))
                    .toList()));
        }
        lab.setDirecteur(dto.directeur() == null || dto.directeur().id() == null ? null : memberRepository.findById(dto.directeur().id()).orElseThrow(() -> new ResourceNotFoundException("Member", "id", dto.directeur().id())));
        lab.setDirecteurAdjoint(dto.directeurAdjoint() == null || dto.directeurAdjoint().id() == null ? null : memberRepository.findById(dto.directeurAdjoint().id()).orElseThrow(() -> new ResourceNotFoundException("Member", "id", dto.directeurAdjoint().id())));

        if (dto.comiteGestion() != null) {
            lab.setComiteGestion(new ArrayList<>(dto.comiteGestion().stream().map(c -> {
                var membre = new com.ensa.labs.recherche.bean.ComiteGestionMembre();
                membre.setNomEnseignant(c.nomEnseignant());
                membre.setRoleComite(c.roleComite());
                return membre;
            }).toList()));
        }
    }

    private Lab get(String id) { return labRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lab", "id", id)); }
    private Lab getByAcronym(String acronym) { return labRepository.findByAcronym(acronym).orElseThrow(() -> new ResourceNotFoundException("Lab", "acronym", acronym)); }
}
