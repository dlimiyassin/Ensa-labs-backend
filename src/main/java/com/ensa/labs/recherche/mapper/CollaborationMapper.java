package com.ensa.labs.recherche.mapper;

import com.ensa.labs.recherche.bean.Collaboration;
import com.ensa.labs.recherche.dto.CollaborationDTO;
import org.springframework.stereotype.Component;

@Component
public class CollaborationMapper {
    public CollaborationDTO toDto(Collaboration collaboration) {
        return new CollaborationDTO(
                collaboration.getId(),
                collaboration.getOrganization(),
                collaboration.getEstablishment(),
                collaboration.getTheme(),
                collaboration.getNature(),
                collaboration.getScope(),
                collaboration.getType(),
                collaboration.getLab() != null ? collaboration.getLab().getAcronym() : null
        );
    }

    public Collaboration toEntity(CollaborationDTO dto) {
        Collaboration collaboration = new Collaboration();
        collaboration.setId(dto.id());
        collaboration.setOrganization(dto.organization());
        collaboration.setEstablishment(dto.establishment());
        collaboration.setTheme(dto.theme());
        collaboration.setNature(dto.nature());
        collaboration.setScope(dto.scope());
        collaboration.setType(dto.type());
        return collaboration;
    }
}
