package com.ensa.labs.recherche.service;

import com.ensa.labs.recherche.bean.Publication;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/** Duplicate detection: same DOI whatever its form, or same title and year. */
class PublicationMatchingTest {

    @Test
    void doiFormsAreEquivalent() {
        assertThat(PublicationMatching.normalizeDoi("https://doi.org/10.1016/J.ENBUILD.2020.1"))
                .isEqualTo("10.1016/j.enbuild.2020.1");
        assertThat(PublicationMatching.normalizeDoi("doi: 10.1016/j.enbuild.2020.1"))
                .isEqualTo("10.1016/j.enbuild.2020.1");
        assertThat(PublicationMatching.normalizeDoi("   ")).isNull();
    }

    @Test
    void titlesIgnoreCaseAccentsAndPunctuation() {
        assertThat(PublicationMatching.normalizeTitle("Étude des ponts : une approche, NOUVELLE!"))
                .isEqualTo("etude des ponts une approche nouvelle");
    }

    @Test
    void matchesOnDoiOrOnTitleAndYear() {
        Publication existing = new Publication();
        existing.setDoi("10.1000/ABC");
        existing.setTitle("Predicting the next word");
        existing.setPublicationYear(2023);

        assertThat(PublicationMatching.sameDoi(existing, PublicationMatching.normalizeDoi("https://dx.doi.org/10.1000/abc"))).isTrue();
        assertThat(PublicationMatching.sameTitleAndYear(existing, PublicationMatching.normalizeTitle("predicting the NEXT word."), 2023)).isTrue();
        assertThat(PublicationMatching.sameTitleAndYear(existing, PublicationMatching.normalizeTitle("Predicting the next word"), 2024)).isFalse();
    }
}
