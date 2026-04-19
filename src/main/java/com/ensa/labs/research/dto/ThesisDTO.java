package com.ensa.labs.research.dto;

import java.time.LocalDate;

public record ThesisDTO(
        String id,
        String author,
        String title,
        LocalDate defenseDate,
        String supervisor,
        String labId
) {
}
