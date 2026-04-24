package com.ensa.labs.recherche.dto;

import java.util.List;

public record EquipeDTO(
        String id,
        String name,
        String labAcronym,
        List<AxeRechercheDTO> axesRecherche,
        MemberDTO responsable,
        List<MemberDTO> members
) {
}
