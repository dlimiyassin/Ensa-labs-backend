package com.ensa.labs.recherche.ws;

import com.ensa.labs.recherche.dto.AxeRechercheDTO;
import com.ensa.labs.recherche.service.AxeRechercheService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/axes-recherche")
public class AxeRechercheController {
    private final AxeRechercheService axeRechercheService;

    public AxeRechercheController(AxeRechercheService axeRechercheService) { this.axeRechercheService = axeRechercheService; }

    @GetMapping
    public List<AxeRechercheDTO> findAll() { return axeRechercheService.findAll(); }

    @GetMapping("/{id}")
    public AxeRechercheDTO findById(@PathVariable String id) { return axeRechercheService.findById(id); }

    @GetMapping("/lab/{labAcronym}")
    public List<AxeRechercheDTO> findByLabAcronym(@PathVariable String labAcronym) { return axeRechercheService.findByLabAcronym(labAcronym); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(Authorities.LAB_MANAGERS)
    public AxeRechercheDTO create(@Valid @RequestBody AxeRechercheDTO dto) { return axeRechercheService.create(dto); }

    @PutMapping("/{id}")
    @PreAuthorize(Authorities.LAB_MANAGERS)
    public AxeRechercheDTO update(@PathVariable String id, @Valid @RequestBody AxeRechercheDTO dto) { return axeRechercheService.update(id, dto); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(Authorities.LAB_MANAGERS)
    public void delete(@PathVariable String id) { axeRechercheService.delete(id); }
}
