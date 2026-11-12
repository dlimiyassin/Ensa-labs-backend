package com.ensa.labs.recherche.service;

import com.ensa.labs.exception.ResourceNotFoundException;
import com.ensa.labs.recherche.bean.Lab;
import com.ensa.labs.recherche.bean.Production;
import com.ensa.labs.recherche.bean.Publication;
import com.ensa.labs.recherche.bean.Thesis;
import com.ensa.labs.recherche.bean.enums.PublicationType;
import com.ensa.labs.recherche.dao.LabRepository;
import com.ensa.labs.recherche.dao.ProductionRepository;
import com.ensa.labs.recherche.dao.PublicationRepository;
import com.ensa.labs.recherche.dao.ThesisRepository;
import com.ensa.labs.recherche.dto.ProductionDTO;
import com.ensa.labs.recherche.mapper.ProductionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

/**
 * A production is the per-lab showcase (publications, communications, theses) displayed on
 * the public "Productions" page. Publications and theses are attached to it automatically.
 */
@Service
public class ProductionService {
    private final ProductionRepository productionRepository;
    private final PublicationRepository publicationRepository;
    private final ThesisRepository thesisRepository;
    private final LabRepository labRepository;
    private final ProductionMapper productionMapper;
    private final AccessControlService accessControlService;

    public ProductionService(ProductionRepository productionRepository, PublicationRepository publicationRepository, ThesisRepository thesisRepository, LabRepository labRepository, ProductionMapper productionMapper, AccessControlService accessControlService) {
        this.productionRepository = productionRepository;
        this.publicationRepository = publicationRepository;
        this.thesisRepository = thesisRepository;
        this.labRepository = labRepository;
        this.productionMapper = productionMapper;
        this.accessControlService = accessControlService;
    }

    public List<ProductionDTO> findAll() { return productionRepository.findAll().stream().map(productionMapper::toDto).toList(); }

    public ProductionDTO findById(String id) { return productionMapper.toDto(get(id)); }

    /** Returns an empty production when the lab exists but has none yet. */
    public ProductionDTO findByLabAcronym(String acronym) {
        return productionRepository.findByLabAcronym(acronym)
                .map(productionMapper::toDto)
                .orElseGet(() -> {
                    Lab lab = labRepository.findByAcronym(acronym).orElseThrow(() -> new ResourceNotFoundException("Lab", "acronym", acronym));
                    return new ProductionDTO(null, List.of(), List.of(), List.of(), lab.getAcronym());
                });
    }

    @Transactional
    public ProductionDTO create(ProductionDTO dto) {
        accessControlService.assertCanEditLabProduction(dto.labAcronym());
        Production production = productionMapper.toEntity(dto);
        applyRelations(production, dto);
        return productionMapper.toDto(productionRepository.save(production));
    }

    @Transactional
    public ProductionDTO update(String id, ProductionDTO dto) {
        Production production = get(id);
        accessControlService.assertCanEditLabProduction(production.getLab().getAcronym());
        accessControlService.assertCanEditLabProduction(dto.labAcronym());
        applyRelations(production, dto);
        return productionMapper.toDto(productionRepository.save(production));
    }

    @Transactional
    public void delete(String id) {
        Production production = get(id);
        accessControlService.assertCanManageLab(production.getLab().getAcronym());
        if (production.getLab() != null) {
            production.getLab().setProduction(null);
        }
        productionRepository.delete(production);
    }

    // ----- used by PublicationService / ThesisService -----

    @Transactional
    public Production getOrCreateForLab(Lab lab) {
        return productionRepository.findByLabAcronym(lab.getAcronym()).orElseGet(() -> {
            Production production = new Production();
            production.setLab(lab);
            Production saved = productionRepository.save(production);
            lab.setProduction(saved);
            return saved;
        });
    }

    /** (Re)places the publication in its lab production: communications or publications by type. */
    @Transactional
    public void attachPublication(Publication publication) {
        detachPublication(publication);
        Production production = getOrCreateForLab(publication.getLab());
        if (publication.getType() == PublicationType.COMMUNICATION) {
            production.getCommunications().add(publication);
        } else {
            production.getPublications().add(publication);
        }
        productionRepository.save(production);
    }

    @Transactional
    public void detachPublication(Publication publication) {
        if (publication.getId() == null) return;
        productionRepository.findByPublicationsId(publication.getId()).forEach(p -> {
            p.getPublications().remove(publication);
            productionRepository.save(p);
        });
        productionRepository.findByCommunicationsId(publication.getId()).forEach(p -> {
            p.getCommunications().remove(publication);
            productionRepository.save(p);
        });
    }

    @Transactional
    public void attachThesis(Thesis thesis) {
        detachThesis(thesis);
        Production production = getOrCreateForLab(thesis.getLab());
        production.getTheses().add(thesis);
        productionRepository.save(production);
    }

    @Transactional
    public void detachThesis(Thesis thesis) {
        if (thesis.getId() == null) return;
        productionRepository.findByThesesId(thesis.getId()).forEach(p -> {
            p.getTheses().remove(thesis);
            productionRepository.save(p);
        });
    }

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
