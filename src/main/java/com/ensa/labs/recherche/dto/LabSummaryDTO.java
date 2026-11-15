package com.ensa.labs.recherche.dto;

import java.util.List;

/**
 * Lightweight view of a lab for the public navigation and the home page (menus, key figures),
 * instead of the full {@link LabDTO} with members, resources and productions.
 *
 * @param axes titles of the lab's research axes
 */
public record LabSummaryDTO(
        String id,
        String acronym,
        String titleFr,
        String titleEn,
        String shortTitle,
        String university,
        String program,
        int membersCount,
        List<String> axes
) {
}
