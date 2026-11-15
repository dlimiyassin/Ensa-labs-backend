package com.ensa.labs.recherche.service;

import com.ensa.labs.recherche.bean.Publication;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Objects;

/** Normalisation used to spot the same publication entered twice (same DOI, or same title and year). */
public final class PublicationMatching {

    private PublicationMatching() {
    }

    /** "https://doi.org/10.1000/XYZ" and "doi:10.1000/xyz" both become "10.1000/xyz"; blank becomes null. */
    public static String normalizeDoi(String doi) {
        if (doi == null) return null;
        String value = doi.trim().toLowerCase(Locale.ROOT)
                .replaceFirst("^https?://(dx\\.)?doi\\.org/", "")
                .replaceFirst("^doi:\\s*", "")
                .trim();
        return value.isEmpty() ? null : value;
    }

    /** Lower case, accents and punctuation removed, single spaces: "Étude, des ponts!" -> "etude des ponts". */
    public static String normalizeTitle(String title) {
        if (title == null) return null;
        String value = Normalizer.normalize(title, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^\\p{L}\\p{N}]+", " ")
                .trim();
        return value.isEmpty() ? null : value;
    }

    public static boolean sameDoi(Publication publication, String normalizedDoi) {
        return normalizedDoi != null && normalizedDoi.equals(normalizeDoi(publication.getDoi()));
    }

    public static boolean sameTitleAndYear(Publication publication, String normalizedTitle, Integer year) {
        return normalizedTitle != null && year != null
                && Objects.equals(year, publication.getPublicationYear())
                && normalizedTitle.equals(normalizeTitle(publication.getTitle()));
    }
}
