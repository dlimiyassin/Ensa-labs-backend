package com.ensa.labs.recherche.dto;

import jakarta.validation.constraints.NotBlank;

/** A research theme ("thématique de recherche"), shared between labs. */
public record ThematiqueRechercheDTO(
        String id,
        @NotBlank String name
) {
}
