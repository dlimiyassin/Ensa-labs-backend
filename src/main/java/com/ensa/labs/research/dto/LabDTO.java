package com.ensa.labs.research.dto;

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
        String directorId,
        String deputyDirectorId,
        List<String> memberIds,
        List<String> comiteGestionNoms,
        List<String> researchFieldIds,
        List<String> researchItemIds,
        List<String> equipmentIds,
        List<String> competenceIds,
        String productionId,
        String departmentId,
        List<String> teamIds,
        List<String> tagIds
) {
}
