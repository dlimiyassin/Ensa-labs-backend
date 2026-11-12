package com.ensa.labs.recherche.ws;

import com.ensa.labs.recherche.dto.ThematiqueRechercheDTO;
import com.ensa.labs.recherche.service.ThematiqueRechercheService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/thematiques-recherche")
public class ThematiqueRechercheController {
    private final ThematiqueRechercheService service;

    public ThematiqueRechercheController(ThematiqueRechercheService service) { this.service = service; }

    @GetMapping
    public List<ThematiqueRechercheDTO> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public ThematiqueRechercheDTO findById(@PathVariable String id) { return service.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(Authorities.SUPER_ADMIN)
    public ThematiqueRechercheDTO create(@Valid @RequestBody ThematiqueRechercheDTO dto) { return service.create(dto); }

    @PutMapping("/{id}")
    @PreAuthorize(Authorities.SUPER_ADMIN)
    public ThematiqueRechercheDTO update(@PathVariable String id, @Valid @RequestBody ThematiqueRechercheDTO dto) { return service.update(id, dto); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(Authorities.SUPER_ADMIN)
    public void delete(@PathVariable String id) { service.delete(id); }
}
