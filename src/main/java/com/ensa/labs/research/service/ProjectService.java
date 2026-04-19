package com.ensa.labs.research.service;

import com.ensa.labs.exception.ResourceNotFoundException;
import com.ensa.labs.research.bean.Project;
import com.ensa.labs.research.dao.LabRepository;
import com.ensa.labs.research.dao.ProjectRepository;
import com.ensa.labs.research.dao.TeamRepository;
import com.ensa.labs.research.dto.ProjectDTO;
import com.ensa.labs.research.mapper.ProjectMapper;
import com.ensa.labs.zBase.security.dao.facade.UserDao;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final LabRepository labRepository;
    private final TeamRepository teamRepository;
    private final UserDao userDao;
    private final ProjectMapper projectMapper;

    public ProjectService(ProjectRepository projectRepository, LabRepository labRepository, TeamRepository teamRepository, UserDao userDao, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.labRepository = labRepository;
        this.teamRepository = teamRepository;
        this.userDao = userDao;
        this.projectMapper = projectMapper;
    }

    public List<ProjectDTO> findAll() { return projectRepository.findAll().stream().map(projectMapper::toDto).toList(); }
    public ProjectDTO findById(String id) { return projectMapper.toDto(get(id)); }

    public ProjectDTO create(ProjectDTO dto) {
        Project project = projectMapper.toEntity(dto);
        applyRelations(project, dto);
        return projectMapper.toDto(projectRepository.save(project));
    }

    public ProjectDTO update(String id, ProjectDTO dto) {
        Project project = get(id);
        project.setTitle(dto.title());
        project.setStatus(dto.status());
        applyRelations(project, dto);
        return projectMapper.toDto(projectRepository.save(project));
    }

    public void delete(String id) { projectRepository.delete(get(id)); }

    private void applyRelations(Project project, ProjectDTO dto) {
        project.setLab(labRepository.findById(dto.labId()).orElseThrow(() -> new ResourceNotFoundException("Lab", "id", dto.labId())));
        project.setTeam(teamRepository.findById(dto.teamId()).orElseThrow(() -> new ResourceNotFoundException("Team", "id", dto.teamId())));
        project.setUsers(new HashSet<>(dto.userIds().stream().map(id -> userDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id))).toList()));
    }

    private Project get(String id) { return projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project", "id", id)); }
}
