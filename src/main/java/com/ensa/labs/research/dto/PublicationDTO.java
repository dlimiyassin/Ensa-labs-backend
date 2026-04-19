package com.ensa.labs.research.dto;

import com.ensa.labs.research.bean.enums.PublicationType;

import java.util.List;

public record PublicationDTO(
        String id,
        String title,
        PublicationType type,
        Integer publicationYear,
        List<String> authors,
        String journal,
        String conference,
        String doi,
        String pages,
        String labId,
        String teamId
) {
}
