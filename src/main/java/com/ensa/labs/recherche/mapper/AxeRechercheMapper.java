package com.ensa.labs.recherche.mapper;

import com.ensa.labs.recherche.bean.AxeRecherche;
import com.ensa.labs.recherche.dto.AxeRechercheDTO;
import org.springframework.stereotype.Component;

@Component
public class AxeRechercheMapper {
    public AxeRechercheDTO toDto(AxeRecherche axeRecherche) {
        return new AxeRechercheDTO(axeRecherche.getId(), axeRecherche.getTitle(), axeRecherche.getLab() != null ? axeRecherche.getLab().getAcronym() : null);
    }
}
