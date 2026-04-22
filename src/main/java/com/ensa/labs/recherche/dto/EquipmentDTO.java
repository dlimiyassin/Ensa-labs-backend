package com.ensa.labs.recherche.dto;

import com.ensa.labs.recherche.bean.enums.EquipmentCategory;

public record EquipmentDTO(
        String id,
        String name,
        EquipmentCategory category,
        String labAcronym
) {
}
