package com.ensa.labs.recherche.mapper;

import com.ensa.labs.recherche.bean.Competence;
import com.ensa.labs.recherche.dto.CompetenceDTO;
import org.springframework.stereotype.Component;

@Component
public class CompetenceMapper {
    public CompetenceDTO toDto(Competence competence) {
        return new CompetenceDTO(competence.getId(), competence.getDescription(), competence.getType(),
                competence.getLab() != null ? competence.getLab().getAcronym() : null);
    }
}
