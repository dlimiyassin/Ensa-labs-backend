package com.ensa.labs.recherche.dto;

import jakarta.validation.constraints.Size;

/** Decision of a reviewer; the comment is required when rejecting. */
public record PublicationReviewDTO(@Size(max = 2000) String comment) {
}
