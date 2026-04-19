package com.ensa.labs.research.dto;

import com.ensa.labs.research.bean.enums.CollaborationScope;

public record CollaborationDTO(
        String id,
        String organization,
        String theme,
        String nature,
        CollaborationScope scope,
        String labId
) {
}
