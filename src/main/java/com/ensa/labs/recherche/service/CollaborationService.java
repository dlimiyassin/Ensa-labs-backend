package com.ensa.labs.recherche.service;

import com.ensa.labs.exception.ResourceNotFoundException;
import com.ensa.labs.recherche.bean.Collaboration;
import com.ensa.labs.recherche.dao.CollaborationRepository;
import com.ensa.labs.recherche.dao.LabRepository;
import com.ensa.labs.recherche.dto.CollaborationDTO;
import com.ensa.labs.recherche.mapper.CollaborationMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollaborationService {
    private final CollaborationRepository collaborationRepository;
    private final LabRepository labRepository;
    private final CollaborationMapper collaborationMapper;

    public CollaborationService(CollaborationRepository collaborationRepository, LabRepository labRepository, CollaborationMapper collaborationMapper) {
        this.collaborationRepository = collaborationRepository;
        this.labRepository = labRepository;
        this.collaborationMapper = collaborationMapper;
    }

    public List<CollaborationDTO> findAll() {
        return collaborationRepository.findAll().stream().map(collaborationMapper::toDto).toList();
    }

    public CollaborationDTO findById(String id) {
        return collaborationMapper.toDto(get(id));
    }

    public List<CollaborationDTO> findByLabAcronym(String acronym) {
        return collaborationRepository.findByLabAcronym(acronym).stream().map(collaborationMapper::toDto).toList();
    }

    public CollaborationDTO create(CollaborationDTO dto) {
        Collaboration collaboration = collaborationMapper.toEntity(dto);
        applyRelations(collaboration, dto);
        return collaborationMapper.toDto(collaborationRepository.save(collaboration));
    }

    public CollaborationDTO update(String id, CollaborationDTO dto) {
        Collaboration collaboration = get(id);
        collaboration.setOrganization(dto.organization());
        collaboration.setEstablishment(dto.establishment());
        collaboration.setTheme(dto.theme());
        collaboration.setNature(dto.nature());
        collaboration.setScope(dto.scope());
        applyRelations(collaboration, dto);
        return collaborationMapper.toDto(collaborationRepository.save(collaboration));
    }

    public void delete(String id) {
        collaborationRepository.delete(get(id));
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
