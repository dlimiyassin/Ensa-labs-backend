package com.ensa.labs.recherche.ws;

import com.ensa.labs.recherche.dto.ThesisDTO;
import com.ensa.labs.recherche.service.ThesisService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theses")
public class ThesisController {
    private final ThesisService thesisService;

    public ThesisController(ThesisService thesisService) { this.thesisService = thesisService; }

    @GetMapping
    public List<ThesisDTO> findAll() { return thesisService.findAll(); }

    @GetMapping("/{id}")
    public ThesisDTO findById(@PathVariable String id) { return thesisService.findById(id); }

    @GetMapping("/lab/{labAcronym}")
    public List<ThesisDTO> findByLabAcronym(@PathVariable String labAcronym) { return thesisService.findByLabAcronym(labAcronym); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(Authorities.PRODUCTION_EDITORS)
    public ThesisDTO create(@Valid @RequestBody ThesisDTO dto) { return thesisService.create(dto); }

    @PutMapping("/{id}")
    @PreAuthorize(Authorities.PRODUCTION_EDITORS)
    public ThesisDTO update(@PathVariable String id, @Valid @RequestBody ThesisDTO dto) { return thesisService.update(id, dto); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(Authorities.LAB_MANAGERS)
    public void delete(@PathVariable String id) { thesisService.delete(id); }
}
