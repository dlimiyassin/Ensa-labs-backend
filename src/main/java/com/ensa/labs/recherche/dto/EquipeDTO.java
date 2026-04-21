package com.ensa.labs.recherche.dto;

import java.util.List;

public record EquipeDTO(
        String id,
        String name,
        String labId,
        String domaineRechercheId,
        String responsableId,
        List<String> memberIds
) {
}
