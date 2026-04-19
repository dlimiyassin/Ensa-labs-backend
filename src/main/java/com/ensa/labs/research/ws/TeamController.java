package com.ensa.labs.research.ws;

import com.ensa.labs.research.dto.TeamDTO;
import com.ensa.labs.research.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) { this.teamService = teamService; }

    @GetMapping
    public List<TeamDTO> findAll() { return teamService.findAll(); }

    @GetMapping("/{id}")
    public TeamDTO findById(@PathVariable String id) { return teamService.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeamDTO create(@RequestBody TeamDTO dto) { return teamService.create(dto); }

    @PutMapping("/{id}")
    public TeamDTO update(@PathVariable String id, @RequestBody TeamDTO dto) { return teamService.update(id, dto); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) { teamService.delete(id); }
}
