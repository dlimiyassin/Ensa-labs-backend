package com.ensa.labs.recherche.service;

import com.ensa.labs.exception.GlobalException;
import com.ensa.labs.exception.ResourceNotFoundException;
import com.ensa.labs.recherche.bean.AxeRecherche;
import com.ensa.labs.recherche.bean.Equipe;
import com.ensa.labs.recherche.dao.AxeRechercheRepository;
import com.ensa.labs.recherche.dao.EquipeRepository;
import com.ensa.labs.recherche.dao.LabRepository;
import com.ensa.labs.recherche.dto.AxeRechercheDTO;
import com.ensa.labs.recherche.mapper.AxeRechercheMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Research axes are attached either directly to a lab or to one of its teams
 * (in which case the lab link stays empty, as in the seeded data).
 */
@Service
public class AxeRechercheService {
    private final AxeRechercheRepository axeRechercheRepository;
    private final LabRepository labRepository;
    private final EquipeRepository equipeRepository;
    private final AxeRechercheMapper axeRechercheMapper;
    private final AccessControlService accessControlService;

    public AxeRechercheService(AxeRechercheRepository axeRechercheRepository, LabRepository labRepository, EquipeRepository equipeRepository, AxeRechercheMapper axeRechercheMapper, AccessControlService accessControlService) {
        this.axeRechercheRepository = axeRechercheRepository;
        this.labRepository = labRepository;
        this.equipeRepository = equipeRepository;
        this.axeRechercheMapper = axeRechercheMapper;
        this.accessControlService = accessControlService;
    }

    public List<AxeRechercheDTO> findAll() { return axeRechercheRepository.findAll().stream().map(axeRechercheMapper::toDto).toList(); }

    public AxeRechercheDTO findById(String id) { return axeRechercheMapper.toDto(get(id)); }

    /** Lab axes plus the axes of the lab's teams. */
    public List<AxeRechercheDTO> findByLabAcronym(String acronym) {
        return axeRechercheRepository.findAll().stream()
                .filter(a -> acronym.equalsIgnoreCase(labAcronymOf(a)))
                .map(axeRechercheMapper::toDto)
                .toList();
    }

    @Transactional
    public AxeRechercheDTO create(AxeRechercheDTO dto) {
        AxeRecherche axe = new AxeRecherche();
        applyOwner(axe, dto);
        accessControlService.assertCanManageLab(labAcronymOf(axe));
        axeRechercheMapper.copyToEntity(dto, axe);
        return axeRechercheMapper.toDto(axeRechercheRepository.save(axe));
    }

    @Transactional
    public AxeRechercheDTO update(String id, AxeRechercheDTO dto) {
        AxeRecherche axe = get(id);
        accessControlService.assertCanManageLab(labAcronymOf(axe));
        applyOwner(axe, dto);
        accessControlService.assertCanManageLab(labAcronymOf(axe));
        axeRechercheMapper.copyToEntity(dto, axe);
        return axeRechercheMapper.toDto(axeRechercheRepository.save(axe));
    }

    @Transactional
    public void delete(String id) {
        AxeRecherche axe = get(id);
        accessControlService.assertCanManageLab(labAcronymOf(axe));
        if (axe.getEquipe() != null) {
            axe.getEquipe().getAxesRecherche().remove(axe);
        }
        axeRechercheRepository.delete(axe);
    }

    private void applyOwner(AxeRecherche axe, AxeRechercheDTO dto) {
        if (dto.equipeId() != null && !dto.equipeId().isBlank()) {
            Equipe equipe = equipeRepository.findById(dto.equipeId()).orElseThrow(() -> new ResourceNotFoundException("Equipe", "id", dto.equipeId()));
            axe.setEquipe(equipe);
            axe.setLab(null);
        } else if (dto.labAcronym() != null && !dto.labAcronym().isBlank()) {
            axe.setLab(labRepository.findByAcronym(dto.labAcronym()).orElseThrow(() -> new ResourceNotFoundException("Lab", "acronym", dto.labAcronym())));
            axe.setEquipe(null);
        } else {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "A research axis needs a lab or a team");
        }
    }

    private String labAcronymOf(AxeRecherche axe) {
        if (axe.getLab() != null) return axe.getLab().getAcronym();
        if (axe.getEquipe() != null && axe.getEquipe().getLab() != null) return axe.getEquipe().getLab().getAcronym();
        return null;
    }

    private AxeRecherche get(String id) { return axeRechercheRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("AxeRecherche", "id", id)); }
}
