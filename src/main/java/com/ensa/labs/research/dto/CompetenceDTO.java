package com.ensa.labs.research.dto;

import com.ensa.labs.research.bean.enums.CompetenceType;

public record CompetenceDTO(
        String id,
        String description,
        CompetenceType type,
        String labId
) {
}
