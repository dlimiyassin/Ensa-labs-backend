package com.ensa.labs.research.mapper;

import com.ensa.labs.research.bean.Team;
import com.ensa.labs.research.dto.TeamDTO;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {
    public TeamDTO toDto(Team team) {
        return new TeamDTO(team.getId(), team.getNom(),
                team.getLaboratoire() != null ? team.getLaboratoire().getId() : null,
                team.getDomaineRecherche() != null ? team.getDomaineRecherche().getId() : null,
                team.getChefEquipe() != null ? team.getChefEquipe().getId() : null,
                team.getMembers().stream().map(m -> m.getId()).toList());
    }

    public Team toEntity(TeamDTO dto) {
        Team team = new Team();
        team.setId(dto.id());
        team.setNom(dto.nom());
        return team;
    }
}
