package com.ensa.labs.research.mapper;

import com.ensa.labs.research.bean.ResearchField;
import com.ensa.labs.research.dto.ResearchFieldDTO;
import org.springframework.stereotype.Component;

@Component
public class ResearchFieldMapper {
    public ResearchFieldDTO toDto(ResearchField researchField) {
        return new ResearchFieldDTO(researchField.getId(), researchField.getName());
    }

    public ResearchField toEntity(ResearchFieldDTO dto) {
        ResearchField researchField = new ResearchField();
        researchField.setId(dto.id());
        researchField.setName(dto.name());
        return researchField;
    }
}
