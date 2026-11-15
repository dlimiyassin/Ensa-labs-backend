package com.ensa.labs.recherche.ws;

import com.ensa.labs.recherche.dto.PublicationDTO;
import com.ensa.labs.recherche.dto.PublicationReviewDTO;
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

    /** Public: validated records only. */
    @GetMapping
    public List<PublicationDTO> findAll() { return publicationService.findAll(); }

    /** Dashboard list with review state, filtered by the caller's scope. */
    @GetMapping("/manage")
    @PreAuthorize(Authorities.PUBLICATION_AUTHORS)
    public List<PublicationDTO> findForManagement() { return publicationService.findForManagement(); }

    /** Possible duplicates (same DOI, or same title and year) of a record being entered. */
    @GetMapping("/duplicates")
    @PreAuthorize(Authorities.PUBLICATION_AUTHORS)
    public List<PublicationDTO> findDuplicates(@RequestParam(required = false) String doi,
                                               @RequestParam(required = false) String title,
                                               @RequestParam(required = false) Integer year,
                                               @RequestParam(required = false) String excludeId) {
        return publicationService.findDuplicates(doi, title, year, excludeId);
    }

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

    @PostMapping("/{id}/validate")
    @PreAuthorize(Authorities.PRODUCTION_EDITORS)
    public PublicationDTO validate(@PathVariable String id, @Valid @RequestBody(required = false) PublicationReviewDTO review) {
        return publicationService.validate(id, review);
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize(Authorities.PRODUCTION_EDITORS)
    public PublicationDTO reject(@PathVariable String id, @Valid @RequestBody PublicationReviewDTO review) {
        return publicationService.reject(id, review);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(Authorities.LAB_MANAGERS)
    public void delete(@PathVariable String id) { publicationService.delete(id); }
}
