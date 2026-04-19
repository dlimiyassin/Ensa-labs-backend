package com.ensa.labs.research.mapper;

import com.ensa.labs.research.bean.Team;
import com.ensa.labs.research.dto.TeamDTO;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {
    public TeamDTO toDto(Team team) {
        return new TeamDTO(team.getId(), team.getName(),
                team.getLab() != null ? team.getLab().getId() : null,
                team.getResearchField() != null ? team.getResearchField().getId() : null,
                team.getLeader() != null ? team.getLeader().getId() : null,
                team.getMembers().stream().map(m -> m.getId()).toList());
    }

    public Team toEntity(TeamDTO dto) {
        Team team = new Team();
        team.setId(dto.id());
        team.setName(dto.name());
        return team;
    }
}
