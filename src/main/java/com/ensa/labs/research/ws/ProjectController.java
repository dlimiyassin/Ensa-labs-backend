package com.ensa.labs.research.ws;

import com.ensa.labs.research.dto.ProjectDTO;
import com.ensa.labs.research.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) { this.projectService = projectService; }

    @GetMapping
    public List<ProjectDTO> findAll() { return projectService.findAll(); }

    @GetMapping("/{id}")
    public ProjectDTO findById(@PathVariable String id) { return projectService.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectDTO create(@RequestBody ProjectDTO dto) { return projectService.create(dto); }

    @PutMapping("/{id}")
    public ProjectDTO update(@PathVariable String id, @RequestBody ProjectDTO dto) { return projectService.update(id, dto); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) { projectService.delete(id); }
}
