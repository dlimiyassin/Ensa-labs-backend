package com.ensa.labs.recherche.ws;

import com.ensa.labs.recherche.dto.LabDTO;
import com.ensa.labs.recherche.service.LabService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/labs")
public class LabController {
    private final LabService labService;

    public LabController(LabService labService) { this.labService = labService; }

    @GetMapping
    public List<LabDTO> findAll() { return labService.findAll(); }

    @GetMapping("/{id}")
    public LabDTO findById(@PathVariable String id) { return labService.findById(id); }

    @GetMapping("/acronym/{acronym}")
    public LabDTO findByAcronym(@PathVariable String acronym) { return labService.findByAcronym(acronym); }

    @GetMapping("/establishment/{establishment}")
    public List<LabDTO> findByEstablishment(@PathVariable String establishment) { return labService.findByEstablishment(establishment); }

    @GetMapping("/department/{departmentName}")
    public List<LabDTO> findByDepartmentName(@PathVariable String departmentName) { return labService.findByDepartmentName(departmentName); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(Authorities.SUPER_ADMIN)
    public LabDTO create(@Valid @RequestBody LabDTO dto) { return labService.create(dto); }

    /** A lab director may update his own lab (checked in the service). */
    @PutMapping("/{id}")
    @PreAuthorize(Authorities.LAB_MANAGERS)
    public LabDTO update(@PathVariable String id, @Valid @RequestBody LabDTO dto) { return labService.update(id, dto); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(Authorities.SUPER_ADMIN)
    public void delete(@PathVariable String id) { labService.delete(id); }
}
