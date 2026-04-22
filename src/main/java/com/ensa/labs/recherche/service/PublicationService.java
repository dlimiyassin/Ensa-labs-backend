package com.ensa.labs.recherche.service;

import com.ensa.labs.exception.ResourceNotFoundException;
import com.ensa.labs.recherche.bean.Publication;
import com.ensa.labs.recherche.dao.EquipeRepository;
import com.ensa.labs.recherche.dao.LabRepository;
import com.ensa.labs.recherche.dao.PublicationRepository;
import com.ensa.labs.recherche.dto.PublicationDTO;
import com.ensa.labs.recherche.mapper.PublicationMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PublicationService {
    private final PublicationRepository publicationRepository;
    private final LabRepository labRepository;
    private final EquipeRepository equipeRepository;
    private final PublicationMapper publicationMapper;

    public PublicationService(PublicationRepository publicationRepository, LabRepository labRepository, EquipeRepository equipeRepository, PublicationMapper publicationMapper) {
        this.publicationRepository = publicationRepository;
        this.labRepository = labRepository;
        this.equipeRepository = equipeRepository;
        this.publicationMapper = publicationMapper;
    }

    public List<PublicationDTO> findAll() { return publicationRepository.findAll().stream().map(publicationMapper::toDto).toList(); }
    public PublicationDTO findById(String id) { return publicationMapper.toDto(get(id)); }
    public List<PublicationDTO> findByLabAcronym(String acronym) { return publicationRepository.findByLabAcronym(acronym).stream().map(publicationMapper::toDto).toList(); }

    public PublicationDTO create(PublicationDTO dto) {
        Publication publication = publicationMapper.toEntity(dto);
        applyRelations(publication, dto);
        return publicationMapper.toDto(publicationRepository.save(publication));
    }

    public PublicationDTO update(String id, PublicationDTO dto) {
        Publication publication = get(id);
        publication.setTitle(dto.title());
        publication.setType(dto.type());
        publication.setPublicationYear(dto.publicationYear());
        publication.setAuthors(dto.authors() == null ? new ArrayList<>() : new ArrayList<>(dto.authors()));
        publication.setJournal(dto.journal());
        publication.setConference(dto.conference());
        publication.setDoi(dto.doi());
        publication.setPages(dto.pages());
        applyRelations(publication, dto);
        return publicationMapper.toDto(publicationRepository.save(publication));
    }

    public void delete(String id) { publicationRepository.delete(get(id)); }

    private void applyRelations(Publication publication, PublicationDTO dto) {
        publication.setLab(labRepository.findByAcronym(dto.labAcronym()).orElseThrow(() -> new ResourceNotFoundException("Lab", "acronym", dto.labAcronym())));
        publication.setEquipe(dto.equipeId() == null ? null : equipeRepository.findById(dto.equipeId()).orElseThrow(() -> new ResourceNotFoundException("Equipe", "id", dto.equipeId())));
    }

    private Publication get(String id) { return publicationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Publication", "id", id)); }
}
