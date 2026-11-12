package com.ensa.labs.recherche.ws;

import com.ensa.labs.recherche.dto.CollaborationDTO;
import com.ensa.labs.recherche.service.CollaborationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collaborations")
public class CollaborationController {

    private final CollaborationService collaborationService;

    public CollaborationController(CollaborationService collaborationService) {
        this.collaborationService = collaborationService;
    }

    @GetMapping
    public List<CollaborationDTO> findAll() {
        return collaborationService.findAll();
    }

    @GetMapping("/academic")
    public List<CollaborationDTO> findAllAcademic() {
        return collaborationService.findAllAcademic();
    }

    @GetMapping("/industrial")
    public List<CollaborationDTO> findAllIndustrial() {
        return collaborationService.findAllIndustrial();
    }

    @GetMapping("/{id}")
    public CollaborationDTO findById(@PathVariable String id) {
        return collaborationService.findById(id);
    }

    @GetMapping("/lab/{labAcronym}")
    public List<CollaborationDTO> findByLabAcronym(@PathVariable String labAcronym) {
        return collaborationService.findByLabAcronym(labAcronym);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(Authorities.LAB_MANAGERS)
    public CollaborationDTO create(@Valid @RequestBody CollaborationDTO dto) {
        return collaborationService.create(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize(Authorities.LAB_MANAGERS)
    public CollaborationDTO update(@PathVariable String id, @Valid @RequestBody CollaborationDTO dto) {
        return collaborationService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(Authorities.LAB_MANAGERS)
    public void delete(@PathVariable String id) {
        collaborationService.delete(id);
    }
}
