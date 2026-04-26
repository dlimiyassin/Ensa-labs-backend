package com.ensa.labs.recherche.dto;

import com.ensa.labs.recherche.bean.enums.CollaborationScope;
import com.ensa.labs.recherche.bean.enums.CollaborationType;

public record CollaborationDTO(
        String id,
        String organization,
        String establishment,
        String theme,
        String nature,
        CollaborationScope scope,
        CollaborationType type,
        String labAcronym
) {
}
