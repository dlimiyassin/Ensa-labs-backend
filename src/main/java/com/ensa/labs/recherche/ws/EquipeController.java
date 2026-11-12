package com.ensa.labs.recherche.ws;

import com.ensa.labs.recherche.dto.EquipeDTO;
import com.ensa.labs.recherche.service.EquipeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipes")
public class EquipeController {
    private final EquipeService equipeService;

    public EquipeController(EquipeService equipeService) { this.equipeService = equipeService; }

    @GetMapping
    public List<EquipeDTO> findAll() { return equipeService.findAll(); }

    @GetMapping("/{id}")
    public EquipeDTO findById(@PathVariable String id) { return equipeService.findById(id); }

    @GetMapping("/lab/{labAcronym}")
    public List<EquipeDTO> findByLabAcronym(@PathVariable String labAcronym) { return equipeService.findByLabAcronym(labAcronym); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(Authorities.LAB_MANAGERS)
    public EquipeDTO create(@Valid @RequestBody EquipeDTO dto) { return equipeService.create(dto); }

    @PutMapping("/{id}")
    @PreAuthorize(Authorities.LAB_MANAGERS)
    public EquipeDTO update(@PathVariable String id, @Valid @RequestBody EquipeDTO dto) { return equipeService.update(id, dto); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(Authorities.LAB_MANAGERS)
    public void delete(@PathVariable String id) { equipeService.delete(id); }
}
