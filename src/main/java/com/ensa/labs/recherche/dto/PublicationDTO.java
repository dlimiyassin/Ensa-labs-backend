package com.ensa.labs.recherche.dto;

import com.ensa.labs.recherche.bean.enums.PublicationType;

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
        String labAcronym,
        String equipeId
) {
}
