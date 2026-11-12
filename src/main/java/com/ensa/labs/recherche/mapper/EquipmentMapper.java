package com.ensa.labs.recherche.mapper;

import com.ensa.labs.recherche.bean.Equipment;
import com.ensa.labs.recherche.dto.EquipmentDTO;
import org.springframework.stereotype.Component;

@Component
public class EquipmentMapper {
    public EquipmentDTO toDto(Equipment equipment) {
        return new EquipmentDTO(equipment.getId(), equipment.getName(), equipment.getCategory(),
                equipment.getLab() != null ? equipment.getLab().getAcronym() : null);
    }

    public void copyToEntity(EquipmentDTO dto, Equipment equipment) {
        equipment.setName(dto.name());
        equipment.setCategory(dto.category());
    }
}
