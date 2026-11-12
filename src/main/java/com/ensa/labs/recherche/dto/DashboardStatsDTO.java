package com.ensa.labs.recherche.dto;

/** Counters displayed on the dashboard home, scoped to one lab when labAcronym is set. */
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
        long competences
) {
}
