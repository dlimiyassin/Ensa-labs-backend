package com.ensa.labs.research.service;

import com.ensa.labs.exception.ResourceNotFoundException;
import com.ensa.labs.research.bean.Team;
import com.ensa.labs.research.dao.LabRepository;
import com.ensa.labs.research.dao.ResearchFieldRepository;
import com.ensa.labs.research.dao.TeamRepository;
import com.ensa.labs.research.dto.TeamDTO;
import com.ensa.labs.research.mapper.TeamMapper;
import com.ensa.labs.zBase.security.dao.facade.UserDao;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final LabRepository labRepository;
    private final ResearchFieldRepository researchFieldRepository;
    private final UserDao userDao;
    private final TeamMapper teamMapper;

    public TeamService(TeamRepository teamRepository, LabRepository labRepository, ResearchFieldRepository researchFieldRepository, UserDao userDao, TeamMapper teamMapper) {
        this.teamRepository = teamRepository;
        this.labRepository = labRepository;
        this.researchFieldRepository = researchFieldRepository;
        this.userDao = userDao;
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
        team.setName(dto.name());
        applyRelations(team, dto);
        return teamMapper.toDto(teamRepository.save(team));
    }

    public void delete(String id) { teamRepository.delete(get(id)); }

    private void applyRelations(Team team, TeamDTO dto) {
        team.setLab(labRepository.findById(dto.labId()).orElseThrow(() -> new ResourceNotFoundException("Lab", "id", dto.labId())));
        team.setResearchField(researchFieldRepository.findById(dto.researchFieldId()).orElseThrow(() -> new ResourceNotFoundException("ResearchField", "id", dto.researchFieldId())));
        team.setLeader(dto.leaderId() == null ? null : userDao.findById(dto.leaderId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", dto.leaderId())));
        team.setMembers(new HashSet<>(dto.memberIds().stream().map(id -> userDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id))).toList()));
    }

    private Team get(String id) { return teamRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Team", "id", id)); }
}
