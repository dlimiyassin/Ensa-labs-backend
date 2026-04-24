package com.ensa.labs.recherche.mapper;

import com.ensa.labs.recherche.bean.Equipe;
import com.ensa.labs.recherche.dto.EquipeDTO;
import org.springframework.stereotype.Component;

@Component
public class EquipeMapper {
    private final AxeRechercheMapper axeRechercheMapper;
    private final MemberMapper memberMapper;

    public EquipeMapper(AxeRechercheMapper axeRechercheMapper, MemberMapper memberMapper) {
        this.axeRechercheMapper = axeRechercheMapper;
        this.memberMapper = memberMapper;
    }

    public EquipeDTO toDto(Equipe equipe) {
        return new EquipeDTO(equipe.getId(), equipe.getName(),
                equipe.getLab() != null ? equipe.getLab().getAcronym() : null,
                equipe.getAxesRecherche().stream().map(axeRechercheMapper::toDto).toList(),
                equipe.getResponsable() != null ? memberMapper.toDto(equipe.getResponsable()) : null,
                equipe.getMembers().stream().map(memberMapper::toDto).toList());
    }

    public Equipe toEntity(EquipeDTO dto) {
        Equipe equipe = new Equipe();
        equipe.setId(dto.id());
        equipe.setName(dto.name());
        return equipe;
    }
}
