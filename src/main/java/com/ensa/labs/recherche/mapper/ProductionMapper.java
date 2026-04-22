package com.ensa.labs.recherche.mapper;

import com.ensa.labs.recherche.bean.Production;
import com.ensa.labs.recherche.dto.ProductionDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductionMapper {
    private final PublicationMapper publicationMapper;
    private final ThesisMapper thesisMapper;

    public ProductionMapper(PublicationMapper publicationMapper, ThesisMapper thesisMapper) {
        this.publicationMapper = publicationMapper;
        this.thesisMapper = thesisMapper;
    }

    public ProductionDTO toDto(Production production) {
        return new ProductionDTO(
                production.getId(),
                production.getPublications().stream().map(publicationMapper::toDto).toList(),
                production.getCommunications().stream().map(publicationMapper::toDto).toList(),
                production.getTheses().stream().map(thesisMapper::toDto).toList(),
                production.getLab() != null ? production.getLab().getAcronym() : null
        );
    }

    public Production toEntity(ProductionDTO dto) {
        Production production = new Production();
        production.setId(dto.id());
        return production;
    }
}
