package com.ensa.labs.recherche.service;

import com.ensa.labs.exception.ResourceNotFoundException;
import com.ensa.labs.recherche.bean.Equipe;
import com.ensa.labs.recherche.dao.LabRepository;
import com.ensa.labs.recherche.dao.DomaineRechercheRepository;
import com.ensa.labs.recherche.dao.EquipeRepository;
import com.ensa.labs.recherche.dto.EquipeDTO;
import com.ensa.labs.recherche.mapper.EquipeMapper;
import com.ensa.labs.zBase.security.dao.facade.UserDao;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class EquipeService {
    private final EquipeRepository equipeRepository;
    private final LabRepository labRepository;
    private final DomaineRechercheRepository domaineRechercheRepository;
    private final UserDao userDao;
    private final EquipeMapper equipeMapper;

    public EquipeService(EquipeRepository equipeRepository, LabRepository labRepository, DomaineRechercheRepository domaineRechercheRepository, UserDao userDao, EquipeMapper equipeMapper) {
        this.equipeRepository = equipeRepository;
        this.labRepository = labRepository;
        this.domaineRechercheRepository = domaineRechercheRepository;
        this.userDao = userDao;
        this.equipeMapper = equipeMapper;
    }

    public List<EquipeDTO> findAll() { return equipeRepository.findAll().stream().map(equipeMapper::toDto).toList(); }
    public EquipeDTO findById(String id) { return equipeMapper.toDto(get(id)); }

    public EquipeDTO create(EquipeDTO dto) {
        Equipe equipe = equipeMapper.toEntity(dto);
        applyRelations(equipe, dto);
        return equipeMapper.toDto(equipeRepository.save(equipe));
    }

    public EquipeDTO update(String id, EquipeDTO dto) {
        Equipe equipe = get(id);
        equipe.setName(dto.name());
        applyRelations(equipe, dto);
        return equipeMapper.toDto(equipeRepository.save(equipe));
    }

    public void delete(String id) { equipeRepository.delete(get(id)); }

    private void applyRelations(Equipe equipe, EquipeDTO dto) {
        equipe.setLab(labRepository.findById(dto.labId()).orElseThrow(() -> new ResourceNotFoundException("Lab", "id", dto.labId())));
        equipe.setDomaineRecherche(domaineRechercheRepository.findById(dto.domaineRechercheId()).orElseThrow(() -> new ResourceNotFoundException("DomaineRecherche", "id", dto.domaineRechercheId())));
        equipe.setResponsable(dto.responsableId() == null ? null : userDao.findById(dto.responsableId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", dto.responsableId())));
        equipe.setMembers(new HashSet<>(dto.memberIds().stream().map(id -> userDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id))).toList()));
    }

    private Equipe get(String id) { return equipeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Equipe", "id", id)); }
}
