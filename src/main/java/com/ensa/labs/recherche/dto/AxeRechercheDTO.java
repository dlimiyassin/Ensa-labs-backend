package com.ensa.labs.recherche.dto;

import jakarta.validation.constraints.NotBlank;

/** A research axis belongs either to a lab (labAcronym) or to a team (equipeId). */
public record AxeRechercheDTO(
        String id,
        @NotBlank String title,
        String labAcronym,
        String equipeId
) {
}
