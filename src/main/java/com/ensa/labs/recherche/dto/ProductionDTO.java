package com.ensa.labs.recherche.dto;

import java.util.List;

public record ProductionDTO(
        String id,
        List<PublicationDTO> publications,
        List<PublicationDTO> communications,
        List<ThesisDTO> theses,
        String labAcronym
) {
}
