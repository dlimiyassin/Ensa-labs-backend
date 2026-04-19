package com.ensa.labs.research.mapper;

import com.ensa.labs.research.bean.Publication;
import com.ensa.labs.research.dto.PublicationDTO;
import org.springframework.stereotype.Component;

@Component
public class PublicationMapper {
    public PublicationDTO toDto(Publication publication) {
        return new PublicationDTO(publication.getId(), publication.getTitle(), publication.getType(), publication.getPublicationYear(),
                publication.getLab() != null ? publication.getLab().getId() : null,
                publication.getTeam() != null ? publication.getTeam().getId() : null,
                publication.getAuthors().stream().map(a -> a.getId()).toList());
    }

    public Publication toEntity(PublicationDTO dto) {
        Publication publication = new Publication();
        publication.setId(dto.id());
        publication.setTitle(dto.title());
        publication.setType(dto.type());
        publication.setPublicationYear(dto.publicationYear());
        return publication;
    }
}
