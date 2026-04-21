package com.ensa.labs.recherche.dto;

import java.time.LocalDate;
import java.util.List;

public record LabDTO(
        String id,
        String titleFr,
        String titleEn,
        String acronym,
        String university,
        String program,
        LocalDate accreditationStart,
        LocalDate accreditationEnd,
        String establishment,
        String phone,
        String email,
        String directeurId,
        String directeurAdjointId,
        List<String> memberIds,
        List<String> comiteGestionNoms,
        List<String> domaineRechercheIds,
        List<String> axeRechercheIds,
        List<String> equipmentIds,
        List<String> competenceIds,
        String productionId,
        String departmentId,
        List<String> equipeIds,
        List<String> tagIds
) {
}
