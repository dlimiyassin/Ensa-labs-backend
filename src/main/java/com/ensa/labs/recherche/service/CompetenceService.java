package com.ensa.labs.recherche.service;

import com.ensa.labs.exception.ResourceNotFoundException;
import com.ensa.labs.recherche.bean.Competence;
import com.ensa.labs.recherche.dao.CompetenceRepository;
import com.ensa.labs.recherche.dao.LabRepository;
import com.ensa.labs.recherche.dto.CompetenceDTO;
import com.ensa.labs.recherche.mapper.CompetenceMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompetenceService {
    private final CompetenceRepository competenceRepository;
    private final LabRepository labRepository;
    private final CompetenceMapper competenceMapper;
    private final AccessControlService accessControlService;

    public CompetenceService(CompetenceRepository competenceRepository, LabRepository labRepository, CompetenceMapper competenceMapper, AccessControlService accessControlService) {
        this.competenceRepository = competenceRepository;
        this.labRepository = labRepository;
        this.competenceMapper = competenceMapper;
        this.accessControlService = accessControlService;
    }

    public List<CompetenceDTO> findAll() { return competenceRepository.findAll().stream().map(competenceMapper::toDto).toList(); }

    public CompetenceDTO findById(String id) { return competenceMapper.toDto(get(id)); }

    public List<CompetenceDTO> findByLabAcronym(String acronym) { return competenceRepository.findByLabAcronym(acronym).stream().map(competenceMapper::toDto).toList(); }

    @Transactional
    public CompetenceDTO create(CompetenceDTO dto) {
        accessControlService.assertCanManageLab(dto.labAcronym());
        Competence competence = new Competence();
        competenceMapper.copyToEntity(dto, competence);
        competence.setLab(labRepository.findByAcronym(dto.labAcronym()).orElseThrow(() -> new ResourceNotFoundException("Lab", "acronym", dto.labAcronym())));
        return competenceMapper.toDto(competenceRepository.save(competence));
    }

    @Transactional
    public CompetenceDTO update(String id, CompetenceDTO dto) {
        Competence competence = get(id);
        accessControlService.assertCanManageLab(competence.getLab().getAcronym());
        accessControlService.assertCanManageLab(dto.labAcronym());
        competenceMapper.copyToEntity(dto, competence);
        competence.setLab(labRepository.findByAcronym(dto.labAcronym()).orElseThrow(() -> new ResourceNotFoundException("Lab", "acronym", dto.labAcronym())));
        return competenceMapper.toDto(competenceRepository.save(competence));
    }

    @Transactional
    public void delete(String id) {
        Competence competence = get(id);
        accessControlService.assertCanManageLab(competence.getLab().getAcronym());
        competenceRepository.delete(competence);
    }

    private Competence get(String id) { return competenceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Competence", "id", id)); }
}
