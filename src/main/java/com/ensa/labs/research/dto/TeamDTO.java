package com.ensa.labs.research.dto;

import java.util.List;

public record TeamDTO(
        String id,
        String nom,
        String laboratoireId,
        String domaineRechercheId,
        String chefEquipeId,
        List<String> membresIds
) {
}
