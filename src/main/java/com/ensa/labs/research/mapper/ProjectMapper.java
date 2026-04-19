package com.ensa.labs.research.mapper;

import com.ensa.labs.research.bean.Project;
import com.ensa.labs.research.dto.ProjectDTO;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {
    public ProjectDTO toDto(Project project) {
        return new ProjectDTO(project.getId(), project.getTitle(), project.getStatus(),
                project.getLab() != null ? project.getLab().getId() : null,
                project.getTeam() != null ? project.getTeam().getId() : null,
                project.getUsers().stream().map(u -> u.getId()).toList());
    }

    public Project toEntity(ProjectDTO dto) {
        Project project = new Project();
        project.setId(dto.id());
        project.setTitle(dto.title());
        project.setStatus(dto.status());
        return project;
    }
}
