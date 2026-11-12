package com.ensa.labs.recherche.mapper;

import com.ensa.labs.recherche.bean.DomaineRecherche;
import com.ensa.labs.recherche.dto.ThematiqueRechercheDTO;
import org.springframework.stereotype.Component;

/** Research themes are stored in the historical {@code domaines_recherche} table. */
@Component
public class ThematiqueRechercheMapper {
    public ThematiqueRechercheDTO toDto(DomaineRecherche domaineRecherche) {
        return new ThematiqueRechercheDTO(domaineRecherche.getId(), domaineRecherche.getName());
    }
}
