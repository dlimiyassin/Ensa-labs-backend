package com.ensa.labs.recherche.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ProductionDTO(
        String id,
        List<PublicationDTO> publications,
        List<PublicationDTO> communications,
        List<ThesisDTO> theses,
        @NotBlank String labAcronym
) {
}
