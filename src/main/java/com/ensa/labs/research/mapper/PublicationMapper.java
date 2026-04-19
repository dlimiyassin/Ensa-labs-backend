package com.ensa.labs.research.mapper;

import com.ensa.labs.research.bean.Publication;
import com.ensa.labs.research.dto.PublicationDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class PublicationMapper {
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
                publication.getLab() != null ? publication.getLab().getId() : null,
                publication.getTeam() != null ? publication.getTeam().getId() : null
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
        return publication;
    }
}
