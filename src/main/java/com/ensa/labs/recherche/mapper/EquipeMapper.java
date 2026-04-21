package com.ensa.labs.recherche.mapper;

import com.ensa.labs.recherche.bean.Equipe;
import com.ensa.labs.recherche.dto.EquipeDTO;
import org.springframework.stereotype.Component;

@Component
public class EquipeMapper {
    public EquipeDTO toDto(Equipe equipe) {
        return new EquipeDTO(equipe.getId(), equipe.getName(),
                equipe.getLab() != null ? equipe.getLab().getId() : null,
                equipe.getDomaineRecherche() != null ? equipe.getDomaineRecherche().getId() : null,
                equipe.getResponsable() != null ? equipe.getResponsable().getId() : null,
                equipe.getMembers().stream().map(m -> m.getId()).toList());
    }

    public Equipe toEntity(EquipeDTO dto) {
        Equipe equipe = new Equipe();
        equipe.setId(dto.id());
        equipe.setName(dto.name());
        return equipe;
    }
}
