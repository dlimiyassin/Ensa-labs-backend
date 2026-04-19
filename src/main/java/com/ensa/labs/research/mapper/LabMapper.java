package com.ensa.labs.research.mapper;

import com.ensa.labs.research.bean.Lab;
import com.ensa.labs.research.dto.LabDTO;
import org.springframework.stereotype.Component;

@Component
public class LabMapper {
    public LabDTO toDto(Lab lab) {
        return new LabDTO(lab.getId(), lab.getName(), lab.getAbbreviation(),
                lab.getDepartment() != null ? lab.getDepartment().getId() : null,
                lab.getResearchFields().stream().map(r -> r.getId()).toList(),
                lab.getTeams().stream().map(t -> t.getId()).toList(),
                lab.getUsers().stream().map(u -> u.getId()).toList(),
                lab.getTags().stream().map(t -> t.getId()).toList());
    }

    public Lab toEntity(LabDTO dto) {
        Lab lab = new Lab();
        lab.setId(dto.id());
        lab.setName(dto.name());
        lab.setAbbreviation(dto.abbreviation());
        return lab;
    }
}
