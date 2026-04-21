package com.ensa.labs.recherche.ws;

import com.ensa.labs.recherche.dto.EquipeDTO;
import com.ensa.labs.recherche.service.EquipeService;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EquipeDTO create(@RequestBody EquipeDTO dto) { return equipeService.create(dto); }

    @PutMapping("/{id}")
    public EquipeDTO update(@PathVariable String id, @RequestBody EquipeDTO dto) { return equipeService.update(id, dto); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) { equipeService.delete(id); }
}
