package com.ensa.labs.recherche.ws;

import com.ensa.labs.recherche.dto.PublicationDTO;
import com.ensa.labs.recherche.service.PublicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publications")
public class PublicationController {
    private final PublicationService publicationService;

    public PublicationController(PublicationService publicationService) { this.publicationService = publicationService; }

    @GetMapping
    public List<PublicationDTO> findAll() { return publicationService.findAll(); }

    @GetMapping("/{id}")
    public PublicationDTO findById(@PathVariable String id) { return publicationService.findById(id); }

    @GetMapping("/lab/{labAcronym}")
    public List<PublicationDTO> findByLabAcronym(@PathVariable String labAcronym) { return publicationService.findByLabAcronym(labAcronym); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(Authorities.PUBLICATION_AUTHORS)
    public PublicationDTO create(@Valid @RequestBody PublicationDTO dto) { return publicationService.create(dto); }

    @PutMapping("/{id}")
    @PreAuthorize(Authorities.PUBLICATION_AUTHORS)
    public PublicationDTO update(@PathVariable String id, @Valid @RequestBody PublicationDTO dto) { return publicationService.update(id, dto); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(Authorities.LAB_MANAGERS)
    public void delete(@PathVariable String id) { publicationService.delete(id); }
}
