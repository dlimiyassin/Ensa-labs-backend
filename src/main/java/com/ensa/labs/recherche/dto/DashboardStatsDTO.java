package com.ensa.labs.recherche.dto;

/**
 * Counters displayed on the dashboard home, scoped to one lab when labAcronym is set.
 * Publications and communications count validated records; pendingReviews counts the records
 * waiting for the caller's approval.
 */
public record DashboardStatsDTO(
        String labAcronym,
        long labs,
        long members,
        long equipes,
        long publications,
        long communications,
        long theses,
        long collaborations,
        long equipments,
        long competences,
        long pendingReviews
) {
}
