package com.ensa.labs.recherche.service;

import com.ensa.labs.exception.GlobalException;
import com.ensa.labs.exception.ResourceNotFoundException;
import com.ensa.labs.recherche.bean.Equipe;
import com.ensa.labs.recherche.bean.Publication;
import com.ensa.labs.recherche.dao.EquipeRepository;
import com.ensa.labs.recherche.dao.LabRepository;
import com.ensa.labs.recherche.dao.PublicationRepository;
import com.ensa.labs.recherche.dto.PublicationDTO;
import com.ensa.labs.recherche.mapper.PublicationMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PublicationService {
    private final PublicationRepository publicationRepository;
    private final LabRepository labRepository;
    private final EquipeRepository equipeRepository;
    private final PublicationMapper publicationMapper;
    private final ProductionService productionService;
    private final AccessControlService accessControlService;

    public PublicationService(PublicationRepository publicationRepository, LabRepository labRepository, EquipeRepository equipeRepository, PublicationMapper publicationMapper, ProductionService productionService, AccessControlService accessControlService) {
        this.publicationRepository = publicationRepository;
        this.labRepository = labRepository;
        this.equipeRepository = equipeRepository;
        this.publicationMapper = publicationMapper;
        this.productionService = productionService;
        this.accessControlService = accessControlService;
    }

    public List<PublicationDTO> findAll() { return publicationRepository.findAll().stream().map(publicationMapper::toDto).toList(); }

    public PublicationDTO findById(String id) { return publicationMapper.toDto(get(id)); }

    public List<PublicationDTO> findByLabAcronym(String acronym) { return publicationRepository.findByLabAcronym(acronym).stream().map(publicationMapper::toDto).toList(); }

    @Transactional
    public PublicationDTO create(PublicationDTO dto) {
        accessControlService.assertCanCreatePublication(dto.labAcronym(), dto.equipeId());
        Publication publication = publicationMapper.toEntity(dto);
        publication.setId(null);
        applyRelations(publication, dto);
        accessControlService.currentUser().ifPresent(publication::setCreatedBy);
        Publication saved = publicationRepository.save(publication);
        productionService.attachPublication(saved);
        return publicationMapper.toDto(saved);
    }

    @Transactional
    public PublicationDTO update(String id, PublicationDTO dto) {
        Publication publication = get(id);
        accessControlService.assertCanUpdatePublication(publication, dto.labAcronym(), dto.equipeId());
        publication.setTitle(dto.title());
        publication.setType(dto.type());
        publication.setPublicationYear(dto.publicationYear());
        publication.setAuthors(dto.authors() == null ? new ArrayList<>() : new ArrayList<>(dto.authors()));
        publication.setJournal(dto.journal());
        publication.setConference(dto.conference());
        publication.setDoi(dto.doi());
        publication.setPages(dto.pages());
        applyRelations(publication, dto);
        Publication saved = publicationRepository.save(publication);
        productionService.attachPublication(saved);
        return publicationMapper.toDto(saved);
    }

    @Transactional
    public void delete(String id) {
        Publication publication = get(id);
        accessControlService.assertCanDeletePublication(publication);
        productionService.detachPublication(publication);
        publicationRepository.delete(publication);
    }

    private void applyRelations(Publication publication, PublicationDTO dto) {
        publication.setLab(labRepository.findByAcronym(dto.labAcronym()).orElseThrow(() -> new ResourceNotFoundException("Lab", "acronym", dto.labAcronym())));
        Equipe equipe = dto.equipeId() == null || dto.equipeId().isBlank() ? null
                : equipeRepository.findById(dto.equipeId()).orElseThrow(() -> new ResourceNotFoundException("Equipe", "id", dto.equipeId()));
        if (equipe != null && !equipe.getLab().getId().equals(publication.getLab().getId())) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "The team does not belong to the selected lab");
        }
        publication.setEquipe(equipe);
    }

    private Publication get(String id) { return publicationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Publication", "id", id)); }
}
