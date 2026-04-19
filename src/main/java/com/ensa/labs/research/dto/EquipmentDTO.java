package com.ensa.labs.research.dto;

import com.ensa.labs.research.bean.enums.EquipmentCategory;

public record EquipmentDTO(
        String id,
        String name,
        EquipmentCategory category,
        String labId
) {
}
