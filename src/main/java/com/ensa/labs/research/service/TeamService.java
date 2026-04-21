package com.ensa.labs.research.service;

import com.ensa.labs.exception.ResourceNotFoundException;
import com.ensa.labs.research.bean.Team;
import com.ensa.labs.research.dao.LabRepository;
import com.ensa.labs.research.dao.MemberRepository;
import com.ensa.labs.research.dao.ResearchFieldRepository;
import com.ensa.labs.research.dao.TeamRepository;
import com.ensa.labs.research.dto.TeamDTO;
import com.ensa.labs.research.mapper.TeamMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final LabRepository labRepository;
    private final ResearchFieldRepository researchFieldRepository;
    private final MemberRepository memberRepository;
    private final TeamMapper teamMapper;

    public TeamService(TeamRepository teamRepository, LabRepository labRepository, ResearchFieldRepository researchFieldRepository, MemberRepository memberRepository, TeamMapper teamMapper) {
        this.teamRepository = teamRepository;
        this.labRepository = labRepository;
        this.researchFieldRepository = researchFieldRepository;
        this.memberRepository = memberRepository;
        this.teamMapper = teamMapper;
    }

    public List<TeamDTO> findAll() { return teamRepository.findAll().stream().map(teamMapper::toDto).toList(); }
    public TeamDTO findById(String id) { return teamMapper.toDto(get(id)); }

    public TeamDTO create(TeamDTO dto) {
        Team team = teamMapper.toEntity(dto);
        applyRelations(team, dto);
        return teamMapper.toDto(teamRepository.save(team));
    }

    public TeamDTO update(String id, TeamDTO dto) {
        Team team = get(id);
        team.setNom(dto.nom());
        applyRelations(team, dto);
        return teamMapper.toDto(teamRepository.save(team));
    }

    public void delete(String id) { teamRepository.delete(get(id)); }

    private void applyRelations(Team team, TeamDTO dto) {
        team.setLaboratoire(labRepository.findById(dto.laboratoireId()).orElseThrow(() -> new ResourceNotFoundException("Lab", "id", dto.laboratoireId())));
        team.setDomaineRecherche(researchFieldRepository.findById(dto.domaineRechercheId()).orElseThrow(() -> new ResourceNotFoundException("ResearchField", "id", dto.domaineRechercheId())));
        team.setChefEquipe(dto.chefEquipeId() == null ? null : memberRepository.findById(dto.chefEquipeId()).orElseThrow(() -> new ResourceNotFoundException("Member", "id", dto.chefEquipeId())));
        team.setMembers(new HashSet<>(dto.membresIds().stream().map(id -> memberRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Member", "id", id))).toList()));
    }

    private Team get(String id) { return teamRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Team", "id", id)); }
}
