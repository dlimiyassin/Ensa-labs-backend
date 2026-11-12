package com.ensa.labs.recherche.service;

import com.ensa.labs.exception.ResourceNotFoundException;
import com.ensa.labs.recherche.bean.Thesis;
import com.ensa.labs.recherche.dao.LabRepository;
import com.ensa.labs.recherche.dao.ThesisRepository;
import com.ensa.labs.recherche.dto.ThesisDTO;
import com.ensa.labs.recherche.mapper.ThesisMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ThesisService {
    private final ThesisRepository thesisRepository;
    private final LabRepository labRepository;
    private final ThesisMapper thesisMapper;
    private final ProductionService productionService;
    private final AccessControlService accessControlService;

    public ThesisService(ThesisRepository thesisRepository, LabRepository labRepository, ThesisMapper thesisMapper, ProductionService productionService, AccessControlService accessControlService) {
        this.thesisRepository = thesisRepository;
        this.labRepository = labRepository;
        this.thesisMapper = thesisMapper;
        this.productionService = productionService;
        this.accessControlService = accessControlService;
    }

    public List<ThesisDTO> findAll() { return thesisRepository.findAll().stream().map(thesisMapper::toDto).toList(); }

    public ThesisDTO findById(String id) { return thesisMapper.toDto(get(id)); }

    public List<ThesisDTO> findByLabAcronym(String acronym) { return thesisRepository.findByLabAcronym(acronym).stream().map(thesisMapper::toDto).toList(); }

    @Transactional
    public ThesisDTO create(ThesisDTO dto) {
        accessControlService.assertCanEditLabProduction(dto.labAcronym());
        Thesis thesis = new Thesis();
        thesisMapper.copyToEntity(dto, thesis);
        thesis.setLab(labRepository.findByAcronym(dto.labAcronym()).orElseThrow(() -> new ResourceNotFoundException("Lab", "acronym", dto.labAcronym())));
        Thesis saved = thesisRepository.save(thesis);
        productionService.attachThesis(saved);
        return thesisMapper.toDto(saved);
    }

    @Transactional
    public ThesisDTO update(String id, ThesisDTO dto) {
        Thesis thesis = get(id);
        accessControlService.assertCanEditLabProduction(thesis.getLab().getAcronym());
        accessControlService.assertCanEditLabProduction(dto.labAcronym());
        thesisMapper.copyToEntity(dto, thesis);
        thesis.setLab(labRepository.findByAcronym(dto.labAcronym()).orElseThrow(() -> new ResourceNotFoundException("Lab", "acronym", dto.labAcronym())));
        Thesis saved = thesisRepository.save(thesis);
        productionService.attachThesis(saved);
        return thesisMapper.toDto(saved);
    }

    @Transactional
    public void delete(String id) {
        Thesis thesis = get(id);
        accessControlService.assertCanManageLab(thesis.getLab().getAcronym());
        productionService.detachThesis(thesis);
        thesisRepository.delete(thesis);
    }

    private Thesis get(String id) { return thesisRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Thesis", "id", id)); }
}
