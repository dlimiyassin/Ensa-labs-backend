package com.ensa.labs.recherche.ws;

import com.ensa.labs.recherche.dto.PublicationDTO;
import com.ensa.labs.recherche.service.PublicationService;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PublicationDTO create(@RequestBody PublicationDTO dto) { return publicationService.create(dto); }

    @PutMapping("/{id}")
    public PublicationDTO update(@PathVariable String id, @RequestBody PublicationDTO dto) { return publicationService.update(id, dto); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) { publicationService.delete(id); }
}
