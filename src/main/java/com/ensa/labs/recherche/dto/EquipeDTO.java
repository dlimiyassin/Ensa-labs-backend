package com.ensa.labs.recherche.dto;

import java.util.List;

public record EquipeDTO(
        String id,
        String name,
        String labAcronym,
        DomaineRechercheDTO domaineRecherche,
        MemberDTO responsable,
        List<MemberDTO> members
) {
}
