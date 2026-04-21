package com.ensa.labs.recherche.mapper;

import com.ensa.labs.recherche.bean.DomaineRecherche;
import com.ensa.labs.recherche.dto.DomaineRechercheDTO;
import org.springframework.stereotype.Component;

@Component
public class DomaineRechercheMapper {
    public DomaineRechercheDTO toDto(DomaineRecherche domaineRecherche) {
        return new DomaineRechercheDTO(domaineRecherche.getId(), domaineRecherche.getName());
    }

    public DomaineRecherche toEntity(DomaineRechercheDTO dto) {
        DomaineRecherche domaineRecherche = new DomaineRecherche();
        domaineRecherche.setId(dto.id());
        domaineRecherche.setName(dto.name());
        return domaineRecherche;
    }
}
