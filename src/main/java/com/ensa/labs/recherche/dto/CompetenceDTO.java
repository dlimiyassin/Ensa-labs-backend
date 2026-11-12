package com.ensa.labs.recherche.dto;

import com.ensa.labs.recherche.bean.enums.CompetenceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CompetenceDTO(
        String id,
        @NotBlank @Size(max = 1500) String description,
        @NotNull CompetenceType type,
        @NotBlank String labAcronym
) {
}
