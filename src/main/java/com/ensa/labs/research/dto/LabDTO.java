package com.ensa.labs.research.dto;

import java.util.List;

public record LabDTO(
        String id,
        String name,
        String abbreviation,
        String departmentId,
        List<String> researchFieldIds,
        List<String> teamIds,
        List<String> userIds,
        List<String> tagIds
) {
}
