package com.ensa.labs.recherche.dto;

import com.ensa.labs.recherche.bean.enums.PublicationStatus;
import com.ensa.labs.recherche.bean.enums.PublicationType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Review fields ({@code status} to {@code canReview}) are read-only: they are set by the
 * server and ignored on create / update. Public endpoints never expose them.
 *
 * @param createdBy      username of the account that created the record
 * @param createdByName  display name of that account
 * @param validatedBy    display name of the reviewer who approved / rejected the record
 * @param canReview      whether the caller may approve or reject this record (dashboard lists only)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PublicationDTO(
        String id,
        @NotBlank @Size(max = 2000) String title,
        @NotNull PublicationType type,
        @Min(1900) @Max(2100) Integer publicationYear,
        List<String> authors,
        String journal,
        String conference,
        @Size(max = 255) String doi,
        String pages,
        @NotBlank String labAcronym,
        String equipeId,
        String createdBy,
        @Size(max = 1000) @Pattern(regexp = "^https?://\\S+$", message = "must be a web address starting with http:// or https://") String link,
        PublicationStatus status,
        String reviewComment,
        String createdByName,
        String validatedBy,
        LocalDateTime validatedAt,
        LocalDateTime submittedAt,
        Boolean canReview
) {
}
