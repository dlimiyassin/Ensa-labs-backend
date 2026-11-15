package com.ensa.labs.recherche.service;

import com.ensa.labs.exception.GlobalException;
import com.ensa.labs.exception.ResourceNotFoundException;
import com.ensa.labs.recherche.bean.Equipe;
import com.ensa.labs.recherche.bean.Publication;
import com.ensa.labs.recherche.bean.enums.PublicationStatus;
import com.ensa.labs.recherche.bean.enums.PublicationType;
import com.ensa.labs.recherche.dao.EquipeRepository;
import com.ensa.labs.recherche.dao.LabRepository;
import com.ensa.labs.recherche.dao.PublicationRepository;
import com.ensa.labs.recherche.dto.PublicationDTO;
import com.ensa.labs.recherche.dto.PublicationReviewDTO;
import com.ensa.labs.recherche.mapper.PublicationMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Publications and communications with their review workflow:
 * <ul>
 *   <li>a record created or edited by someone who cannot review it (a researcher) is {@code PENDING};</li>
 *   <li>a record created by a reviewer (super admin, lab director, leader of its team) is {@code VALIDATED} directly;</li>
 *   <li>reviewers approve or reject pending records, with a comment (required to reject);</li>
 *   <li>only validated records are returned by the public endpoints.</li>
 * </ul>
 */
@Service
public class PublicationService {
    private final PublicationRepository publicationRepository;
    private final LabRepository labRepository;
    private final EquipeRepository equipeRepository;
    private final PublicationMapper publicationMapper;
    private final ProductionService productionService;
    private final AccessControlService accessControlService;

    public PublicationService(PublicationRepository publicationRepository, LabRepository labRepository, EquipeRepository equipeRepository, PublicationMapper publicationMapper, ProductionService productionService, AccessControlService accessControlService) {
        this.publicationRepository = publicationRepository;
        this.labRepository = labRepository;
        this.equipeRepository = equipeRepository;
        this.publicationMapper = publicationMapper;
        this.productionService = productionService;
        this.accessControlService = accessControlService;
    }

    // ----- public website (validated records only) -----

    public List<PublicationDTO> findAll() {
        return publicationRepository.findAll().stream().filter(Publication::isPublic).map(publicationMapper::toDto).toList();
    }

    public PublicationDTO findById(String id) {
        Publication publication = get(id);
        if (!publication.isPublic()) {
            throw new ResourceNotFoundException("Publication", "id", id);
        }
        return publicationMapper.toDto(publication);
    }

    public List<PublicationDTO> findByLabAcronym(String acronym) {
        return publicationRepository.findByLabAcronym(acronym).stream().filter(Publication::isPublic).map(publicationMapper::toDto).toList();
    }

    // ----- dashboard -----

    /** Records the caller may see in the dashboard, with their review state; newest submissions first. */
    @Transactional(readOnly = true)
    public List<PublicationDTO> findForManagement() {
        AccessControlService.Scope scope = accessControlService.currentScope();
        return publicationRepository.findAll().stream()
                .filter(publication -> accessControlService.canSeeInDashboard(scope, publication))
                .sorted(Comparator.comparing(Publication::getSubmittedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(publication -> toManagementDto(scope, publication))
                .toList();
    }

    /**
     * Possible duplicates of a record being entered: same DOI, or same title (ignoring case,
     * accents and punctuation) and same year. Rejected records are ignored.
     */
    @Transactional(readOnly = true)
    public List<PublicationDTO> findDuplicates(String doi, String title, Integer year, String excludeId) {
        AccessControlService.Scope scope = accessControlService.currentScope();
        String normalizedDoi = PublicationMatching.normalizeDoi(doi);
        String normalizedTitle = PublicationMatching.normalizeTitle(title);
        if (normalizedDoi == null && (normalizedTitle == null || year == null)) {
            return List.of();
        }
        return publicationRepository.findAll().stream()
                .filter(publication -> !Objects.equals(publication.getId(), excludeId))
                .filter(publication -> publication.getStatus() != PublicationStatus.REJECTED)
                .filter(publication -> PublicationMatching.sameDoi(publication, normalizedDoi)
                        || PublicationMatching.sameTitleAndYear(publication, normalizedTitle, year))
                .map(publication -> toManagementDto(scope, publication))
                .toList();
    }

    @Transactional
    public PublicationDTO create(PublicationDTO dto) {
        accessControlService.assertCanCreatePublication(dto.labAcronym(), dto.equipeId());
        assertSourceProvided(dto);
        AccessControlService.Scope scope = accessControlService.currentScope();
        Publication publication = publicationMapper.toEntity(dto);
        publication.setId(null);
        publication.setDoi(cleanDoi(dto.doi()));
        publication.setLink(blankToNull(dto.link()));
        applyRelations(publication, dto);
        publication.setCreatedBy(scope.user());
        publication.setSubmittedAt(LocalDateTime.now());
        if (accessControlService.canReviewPublication(scope, dto.labAcronym(), dto.equipeId())) {
            markValidated(scope, publication, null);
        } else {
            markPending(publication);
        }
        Publication saved = publicationRepository.save(publication);
        productionService.attachPublication(saved);
        return toManagementDto(scope, saved);
    }

    /**
     * A reviewer's edit keeps the current status (approval is an explicit action); an edit by
     * the author sends the record back to review, even if it was validated.
     */
    @Transactional
    public PublicationDTO update(String id, PublicationDTO dto) {
        Publication publication = get(id);
        accessControlService.assertCanUpdatePublication(publication, dto.labAcronym(), dto.equipeId());
        assertSourceProvided(dto);
        AccessControlService.Scope scope = accessControlService.currentScope();
        publication.setTitle(dto.title());
        publication.setType(dto.type());
        publication.setPublicationYear(dto.publicationYear());
        publication.setAuthors(dto.authors() == null ? new ArrayList<>() : new ArrayList<>(dto.authors()));
        publication.setJournal(dto.journal());
        publication.setConference(dto.conference());
        publication.setDoi(cleanDoi(dto.doi()));
        publication.setPages(dto.pages());
        publication.setLink(blankToNull(dto.link()));
        applyRelations(publication, dto);
        if (!accessControlService.canReviewPublication(scope, dto.labAcronym(), dto.equipeId())) {
            publication.setSubmittedAt(LocalDateTime.now());
            markPending(publication);
        } else if (publication.getStatus() == null) {
            markValidated(scope, publication, null);
        }
        Publication saved = publicationRepository.save(publication);
        productionService.attachPublication(saved);
        return toManagementDto(scope, saved);
    }

    @Transactional
    public PublicationDTO validate(String id, PublicationReviewDTO review) {
        Publication publication = get(id);
        AccessControlService.Scope scope = accessControlService.currentScope();
        accessControlService.assertCanReviewPublication(scope, publication);
        markValidated(scope, publication, review == null ? null : blankToNull(review.comment()));
        return toManagementDto(scope, publicationRepository.save(publication));
    }

    @Transactional
    public PublicationDTO reject(String id, PublicationReviewDTO review) {
        Publication publication = get(id);
        AccessControlService.Scope scope = accessControlService.currentScope();
        accessControlService.assertCanReviewPublication(scope, publication);
        String comment = review == null ? null : blankToNull(review.comment());
        if (comment == null) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "A comment is required to reject a publication");
        }
        publication.setStatus(PublicationStatus.REJECTED);
        publication.setReviewComment(comment);
        publication.setValidatedBy(scope.user());
        publication.setValidatedAt(LocalDateTime.now());
        return toManagementDto(scope, publicationRepository.save(publication));
    }

    @Transactional
    public void delete(String id) {
        Publication publication = get(id);
        accessControlService.assertCanDeletePublication(publication);
        productionService.detachPublication(publication);
        publicationRepository.delete(publication);
    }

    // ----- helpers -----

    private PublicationDTO toManagementDto(AccessControlService.Scope scope, Publication publication) {
        boolean canReview = accessControlService.canReviewPublication(scope,
                publication.getLab() != null ? publication.getLab().getAcronym() : null,
                publication.getEquipe() != null ? publication.getEquipe().getId() : null);
        return publicationMapper.toManagementDto(publication, canReview);
    }

    private static void markValidated(AccessControlService.Scope scope, Publication publication, String comment) {
        publication.setStatus(PublicationStatus.VALIDATED);
        publication.setReviewComment(comment);
        publication.setValidatedBy(scope.user());
        publication.setValidatedAt(LocalDateTime.now());
    }

    private static void markPending(Publication publication) {
        publication.setStatus(PublicationStatus.PENDING);
        publication.setReviewComment(null);
        publication.setValidatedBy(null);
        publication.setValidatedAt(null);
    }

    /** Journal articles must be traceable: a DOI or a link to the article is required. */
    private static void assertSourceProvided(PublicationDTO dto) {
        if (dto.type() == PublicationType.JOURNAL && blankToNull(dto.doi()) == null && blankToNull(dto.link()) == null) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "A DOI or a link is required for a journal article");
        }
    }

    /** Stores the bare DOI ("10.xxxx/...") whatever form was typed. */
    private static String cleanDoi(String doi) {
        String trimmed = blankToNull(doi);
        if (trimmed == null) return null;
        return trimmed.replaceFirst("(?i)^https?://(dx\\.)?doi\\.org/", "").replaceFirst("(?i)^doi:\\s*", "").trim();
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private void applyRelations(Publication publication, PublicationDTO dto) {
        publication.setLab(labRepository.findByAcronym(dto.labAcronym()).orElseThrow(() -> new ResourceNotFoundException("Lab", "acronym", dto.labAcronym())));
        Equipe equipe = dto.equipeId() == null || dto.equipeId().isBlank() ? null
                : equipeRepository.findById(dto.equipeId()).orElseThrow(() -> new ResourceNotFoundException("Equipe", "id", dto.equipeId()));
        if (equipe != null && !equipe.getLab().getId().equals(publication.getLab().getId())) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "The team does not belong to the selected lab");
        }
        publication.setEquipe(equipe);
    }

    private Publication get(String id) { return publicationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Publication", "id", id)); }
}
