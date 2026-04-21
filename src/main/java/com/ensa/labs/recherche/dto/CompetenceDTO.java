package com.ensa.labs.recherche.dto;

import com.ensa.labs.recherche.bean.enums.CompetenceType;

public record CompetenceDTO(
        String id,
        String description,
        CompetenceType type,
        String labId
) {
}
