package com.ensa.labs.recherche.dto;

import java.util.List;

public record ProductionDTO(
        String id,
        List<String> publicationIds,
        List<String> communicationIds,
        List<String> thesisIds,
        String labId
) {
}
