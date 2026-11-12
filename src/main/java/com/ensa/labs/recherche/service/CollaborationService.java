package com.ensa.labs.recherche.service;

import com.ensa.labs.exception.ResourceNotFoundException;
import com.ensa.labs.recherche.bean.Collaboration;
import com.ensa.labs.recherche.bean.enums.CollaborationType;
import com.ensa.labs.recherche.dao.CollaborationRepository;
import com.ensa.labs.recherche.dao.LabRepository;
import com.ensa.labs.recherche.dto.CollaborationDTO;
import com.ensa.labs.recherche.mapper.CollaborationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CollaborationService {
    private final CollaborationRepository collaborationRepository;
    private final LabRepository labRepository;
    private final CollaborationMapper collaborationMapper;
    private final AccessControlService accessControlService;

    public CollaborationService(CollaborationRepository collaborationRepository, LabRepository labRepository, CollaborationMapper collaborationMapper, AccessControlService accessControlService) {
        this.collaborationRepository = collaborationRepository;
        this.labRepository = labRepository;
        this.collaborationMapper = collaborationMapper;
        this.accessControlService = accessControlService;
    }

    public List<CollaborationDTO> findAll() {
        return collaborationRepository.findAll().stream().map(collaborationMapper::toDto).toList();
    }

    public List<CollaborationDTO> findAllAcademic() {
        return collaborationRepository.findAll().stream()
                .filter(c -> c.getType() == CollaborationType.ACADEMIC)
                .map(collaborationMapper::toDto)
                .toList();
    }

    public List<CollaborationDTO> findAllIndustrial() {
        return collaborationRepository.findAll().stream()
                .filter(c -> c.getType() == CollaborationType.INDUSTRIAL)
                .map(collaborationMapper::toDto)
                .toList();
    }

    public CollaborationDTO findById(String id) {
        return collaborationMapper.toDto(get(id));
    }

    public List<CollaborationDTO> findByLabAcronym(String acronym) {
        return collaborationRepository.findByLabAcronym(acronym).stream().map(collaborationMapper::toDto).toList();
    }

    @Transactional
    public CollaborationDTO create(CollaborationDTO dto) {
        accessControlService.assertCanManageLab(dto.labAcronym());
        Collaboration collaboration = collaborationMapper.toEntity(dto);
        collaboration.setId(null);
        applyRelations(collaboration, dto);
        return collaborationMapper.toDto(collaborationRepository.save(collaboration));
    }

    @Transactional
    public CollaborationDTO update(String id, CollaborationDTO dto) {
        Collaboration collaboration = get(id);
        accessControlService.assertCanManageLab(collaboration.getLab().getAcronym());
        accessControlService.assertCanManageLab(dto.labAcronym());
        collaboration.setOrganization(dto.organization());
        collaboration.setEstablishment(dto.establishment());
        collaboration.setTheme(dto.theme());
        collaboration.setNature(dto.nature());
        collaboration.setScope(dto.scope());
        collaboration.setType(dto.type());
        collaboration.setLogo(dto.logo());
        applyRelations(collaboration, dto);
        return collaborationMapper.toDto(collaborationRepository.save(collaboration));
    }

    @Transactional
    public void delete(String id) {
        Collaboration collaboration = get(id);
        accessControlService.assertCanManageLab(collaboration.getLab().getAcronym());
        collaborationRepository.delete(collaboration);
    }

    private void applyRelations(Collaboration collaboration, CollaborationDTO dto) {
        collaboration.setLab(labRepository.findByAcronym(dto.labAcronym())
                .orElseThrow(() -> new ResourceNotFoundException("Lab", "acronym", dto.labAcronym())));
    }

    private Collaboration get(String id) {
        return collaborationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Collaboration", "id", id));
    }
}
