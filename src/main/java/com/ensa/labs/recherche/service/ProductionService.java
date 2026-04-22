package com.ensa.labs.recherche.service;

import com.ensa.labs.exception.ResourceNotFoundException;
import com.ensa.labs.recherche.bean.Production;
import com.ensa.labs.recherche.dao.LabRepository;
import com.ensa.labs.recherche.dao.ProductionRepository;
import com.ensa.labs.recherche.dao.PublicationRepository;
import com.ensa.labs.recherche.dao.ThesisRepository;
import com.ensa.labs.recherche.dto.ProductionDTO;
import com.ensa.labs.recherche.mapper.ProductionMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class ProductionService {
    private final ProductionRepository productionRepository;
    private final PublicationRepository publicationRepository;
    private final ThesisRepository thesisRepository;
    private final LabRepository labRepository;
    private final ProductionMapper productionMapper;

    public ProductionService(ProductionRepository productionRepository, PublicationRepository publicationRepository, ThesisRepository thesisRepository, LabRepository labRepository, ProductionMapper productionMapper) {
        this.productionRepository = productionRepository;
        this.publicationRepository = publicationRepository;
        this.thesisRepository = thesisRepository;
        this.labRepository = labRepository;
        this.productionMapper = productionMapper;
    }

    public List<ProductionDTO> findAll() { return productionRepository.findAll().stream().map(productionMapper::toDto).toList(); }
    public ProductionDTO findById(String id) { return productionMapper.toDto(get(id)); }
    public ProductionDTO findByLabAcronym(String acronym) { return productionMapper.toDto(productionRepository.findByLabAcronym(acronym).orElseThrow(() -> new ResourceNotFoundException("Production", "labAcronym", acronym))); }

    public ProductionDTO create(ProductionDTO dto) {
        Production production = productionMapper.toEntity(dto);
        applyRelations(production, dto);
        return productionMapper.toDto(productionRepository.save(production));
    }

    public ProductionDTO update(String id, ProductionDTO dto) {
        Production production = get(id);
        applyRelations(production, dto);
        return productionMapper.toDto(productionRepository.save(production));
    }

    public void delete(String id) { productionRepository.delete(get(id)); }

    private void applyRelations(Production production, ProductionDTO dto) {
        production.setLab(labRepository.findByAcronym(dto.labAcronym()).orElseThrow(() -> new ResourceNotFoundException("Lab", "acronym", dto.labAcronym())));
        production.setPublications(dto.publications() == null ? new HashSet<>() : new HashSet<>(dto.publications().stream()
                .filter(p -> p.id() != null)
                .map(p -> publicationRepository.findById(p.id()).orElseThrow(() -> new ResourceNotFoundException("Publication", "id", p.id())))
                .toList()));
        production.setCommunications(dto.communications() == null ? new HashSet<>() : new HashSet<>(dto.communications().stream()
                .filter(p -> p.id() != null)
                .map(p -> publicationRepository.findById(p.id()).orElseThrow(() -> new ResourceNotFoundException("Publication", "id", p.id())))
                .toList()));
        production.setTheses(dto.theses() == null ? new HashSet<>() : new HashSet<>(dto.theses().stream()
                .filter(t -> t.id() != null)
                .map(t -> thesisRepository.findById(t.id()).orElseThrow(() -> new ResourceNotFoundException("Thesis", "id", t.id())))
                .toList()));
    }

    private Production get(String id) { return productionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Production", "id", id)); }
}
