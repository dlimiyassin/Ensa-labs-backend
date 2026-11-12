package com.ensa.labs.recherche.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record EquipeDTO(
        String id,
        @NotBlank String name,
        @NotBlank String labAcronym,
        List<AxeRechercheDTO> axesRecherche,
        MemberDTO responsable,
        List<MemberDTO> members
) {
}
