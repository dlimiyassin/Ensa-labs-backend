package com.ensa.labs.research.dto;

import com.ensa.labs.research.bean.enums.ResearchItemType;

public record ResearchItemDTO(
        String id,
        String title,
        ResearchItemType type,
        String labId
) {
}
