package com.ensa.labs.recherche.dto;

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
