package com.ensa.labs.recherche.dto;

import com.ensa.labs.recherche.bean.enums.EquipmentCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EquipmentDTO(
        String id,
        @NotBlank String name,
        @NotNull EquipmentCategory category,
        @NotBlank String labAcronym
) {
}
