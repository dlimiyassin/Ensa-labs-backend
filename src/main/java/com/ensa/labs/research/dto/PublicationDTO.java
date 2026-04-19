package com.ensa.labs.research.dto;

import com.ensa.labs.research.bean.enums.PublicationType;

import java.util.List;

public record PublicationDTO(
        String id,
        String title,
        PublicationType type,
        Integer publicationYear,
        String labId,
        String teamId,
        List<String> authorIds
) {
}
