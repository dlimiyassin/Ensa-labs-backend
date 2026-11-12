package com.ensa.labs.recherche.dto;

import com.ensa.labs.recherche.bean.enums.PublicationType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * @param createdBy username of the account that created the record (read-only)
 */
public record PublicationDTO(
        String id,
        @NotBlank @Size(max = 2000) String title,
        @NotNull PublicationType type,
        @Min(1900) @Max(2100) Integer publicationYear,
        List<String> authors,
        String journal,
        String conference,
        String doi,
        String pages,
        @NotBlank String labAcronym,
        String equipeId,
        String createdBy
) {
}
