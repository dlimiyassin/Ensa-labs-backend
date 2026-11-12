package com.ensa.labs.recherche.dto;

import com.ensa.labs.recherche.bean.enums.CollaborationScope;
import com.ensa.labs.recherche.bean.enums.CollaborationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CollaborationDTO(
        String id,
        @NotBlank String organization,
        String establishment,
        String theme,
        String nature,
        @NotNull CollaborationScope scope,
        @NotNull CollaborationType type,
        @NotBlank String labAcronym,
        String logo
) {
}
