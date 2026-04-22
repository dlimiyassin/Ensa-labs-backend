package com.ensa.labs.recherche.ws;

import com.ensa.labs.recherche.dto.LabDTO;
import com.ensa.labs.recherche.service.LabService;
import org.springframework.http.HttpStatus;
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
    public LabDTO create(@RequestBody LabDTO dto) { return labService.create(dto); }

    @PutMapping("/{id}")
    public LabDTO update(@PathVariable String id, @RequestBody LabDTO dto) { return labService.update(id, dto); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) { labService.delete(id); }
}
