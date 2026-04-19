package com.ensa.labs.research.dto;

import java.util.List;

public record TeamDTO(
        String id,
        String name,
        String labId,
        String researchFieldId,
        String leaderId,
        List<String> memberIds
) {
}
