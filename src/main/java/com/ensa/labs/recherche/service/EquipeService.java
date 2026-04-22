package com.ensa.labs.recherche.service;

import com.ensa.labs.exception.ResourceNotFoundException;
import com.ensa.labs.recherche.bean.Equipe;
import com.ensa.labs.recherche.dao.DomaineRechercheRepository;
import com.ensa.labs.recherche.dao.EquipeRepository;
import com.ensa.labs.recherche.dao.LabRepository;
import com.ensa.labs.recherche.dao.MemberRepository;
import com.ensa.labs.recherche.dto.EquipeDTO;
import com.ensa.labs.recherche.mapper.EquipeMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class EquipeService {
    private final EquipeRepository equipeRepository;
    private final LabRepository labRepository;
    private final DomaineRechercheRepository domaineRechercheRepository;
    private final MemberRepository memberRepository;
    private final EquipeMapper equipeMapper;

    public EquipeService(EquipeRepository equipeRepository, LabRepository labRepository, DomaineRechercheRepository domaineRechercheRepository, MemberRepository memberRepository, EquipeMapper equipeMapper) {
        this.equipeRepository = equipeRepository;
        this.labRepository = labRepository;
        this.domaineRechercheRepository = domaineRechercheRepository;
        this.memberRepository = memberRepository;
        this.equipeMapper = equipeMapper;
    }

    public List<EquipeDTO> findAll() { return equipeRepository.findAll().stream().map(equipeMapper::toDto).toList(); }
    public EquipeDTO findById(String id) { return equipeMapper.toDto(get(id)); }
    public List<EquipeDTO> findByLabAcronym(String acronym) { return equipeRepository.findByLabAcronym(acronym).stream().map(equipeMapper::toDto).toList(); }

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
        equipe.setLab(labRepository.findByAcronym(dto.labAcronym()).orElseThrow(() -> new ResourceNotFoundException("Lab", "acronym", dto.labAcronym())));
        if (dto.domaineRecherche() != null && dto.domaineRecherche().id() != null) {
            equipe.setDomaineRecherche(domaineRechercheRepository.findById(dto.domaineRecherche().id()).orElseThrow(() -> new ResourceNotFoundException("DomaineRecherche", "id", dto.domaineRecherche().id())));
        }
        equipe.setResponsable(dto.responsable() == null || dto.responsable().id() == null ? null : memberRepository.findById(dto.responsable().id()).orElseThrow(() -> new ResourceNotFoundException("Member", "id", dto.responsable().id())));
        equipe.setMembers(dto.members() == null ? new HashSet<>() : new HashSet<>(dto.members().stream()
                .filter(m -> m.id() != null)
                .map(m -> memberRepository.findById(m.id()).orElseThrow(() -> new ResourceNotFoundException("Member", "id", m.id())))
                .toList()));
    }

    private Equipe get(String id) { return equipeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Equipe", "id", id)); }
}
