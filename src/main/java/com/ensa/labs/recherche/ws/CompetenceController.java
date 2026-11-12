package com.ensa.labs.recherche.ws;

import com.ensa.labs.recherche.dto.CompetenceDTO;
import com.ensa.labs.recherche.service.CompetenceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/competences")
public class CompetenceController {
    private final CompetenceService competenceService;

    public CompetenceController(CompetenceService competenceService) { this.competenceService = competenceService; }

    @GetMapping
    public List<CompetenceDTO> findAll() { return competenceService.findAll(); }

    @GetMapping("/{id}")
    public CompetenceDTO findById(@PathVariable String id) { return competenceService.findById(id); }

    @GetMapping("/lab/{labAcronym}")
    public List<CompetenceDTO> findByLabAcronym(@PathVariable String labAcronym) { return competenceService.findByLabAcronym(labAcronym); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(Authorities.LAB_MANAGERS)
    public CompetenceDTO create(@Valid @RequestBody CompetenceDTO dto) { return competenceService.create(dto); }

    @PutMapping("/{id}")
    @PreAuthorize(Authorities.LAB_MANAGERS)
    public CompetenceDTO update(@PathVariable String id, @Valid @RequestBody CompetenceDTO dto) { return competenceService.update(id, dto); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(Authorities.LAB_MANAGERS)
    public void delete(@PathVariable String id) { competenceService.delete(id); }
}
