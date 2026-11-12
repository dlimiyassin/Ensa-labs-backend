package com.ensa.labs.recherche.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ThesisDTO(
        String id,
        @NotBlank String author,
        @NotBlank @Size(max = 2000) String title,
        LocalDate defenseDate,
        String supervisor,
        @NotBlank String labAcronym
) {
}
