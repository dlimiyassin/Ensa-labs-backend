package com.ensa.labs.recherche.mapper;

import com.ensa.labs.recherche.bean.Publication;
import com.ensa.labs.recherche.dto.PublicationDTO;
import com.ensa.labs.zBase.security.bean.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class PublicationMapper {

    /** Public view: bibliographic data only, no review details. */
    public PublicationDTO toDto(Publication publication) {
        return new PublicationDTO(
                publication.getId(),
                publication.getTitle(),
                publication.getType(),
                publication.getPublicationYear(),
                publication.getAuthors(),
                publication.getJournal(),
                publication.getConference(),
                publication.getDoi(),
                publication.getPages(),
                publication.getLab() != null ? publication.getLab().getAcronym() : null,
                publication.getEquipe() != null ? publication.getEquipe().getId() : null,
                null,
                publication.getLink(),
                null, null, null, null, null, null, null
        );
    }

    /** Dashboard view: includes the review state and who did what. */
    public PublicationDTO toManagementDto(Publication publication, boolean canReview) {
        return new PublicationDTO(
                publication.getId(),
                publication.getTitle(),
                publication.getType(),
                publication.getPublicationYear(),
                publication.getAuthors(),
                publication.getJournal(),
                publication.getConference(),
                publication.getDoi(),
                publication.getPages(),
                publication.getLab() != null ? publication.getLab().getAcronym() : null,
                publication.getEquipe() != null ? publication.getEquipe().getId() : null,
                publication.getCreatedBy() != null ? publication.getCreatedBy().getUsername() : null,
                publication.getLink(),
                publication.getStatus(),
                publication.getReviewComment(),
                displayName(publication.getCreatedBy()),
                displayName(publication.getValidatedBy()),
                publication.getValidatedAt(),
                publication.getSubmittedAt(),
                canReview
        );
    }

    public Publication toEntity(PublicationDTO dto) {
        Publication publication = new Publication();
        publication.setId(dto.id());
        publication.setTitle(dto.title());
        publication.setType(dto.type());
        publication.setPublicationYear(dto.publicationYear());
        publication.setAuthors(dto.authors() == null ? new ArrayList<>() : new ArrayList<>(dto.authors()));
        publication.setJournal(dto.journal());
        publication.setConference(dto.conference());
        publication.setDoi(dto.doi());
        publication.setPages(dto.pages());
        publication.setLink(dto.link());
        return publication;
    }

    private static String displayName(User user) {
        if (user == null) return null;
        String fullName = ((user.getFirstName() == null ? "" : user.getFirstName()) + " "
                + (user.getLastName() == null ? "" : user.getLastName())).trim();
        return fullName.isEmpty() ? user.getUsername() : fullName;
    }
}
