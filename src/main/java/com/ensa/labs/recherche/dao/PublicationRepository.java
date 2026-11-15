package com.ensa.labs.recherche.dao;

import com.ensa.labs.recherche.bean.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PublicationRepository extends JpaRepository<Publication, String> {
    List<Publication> findByLabAcronym(String acronym);
    List<Publication> findByEquipeId(String equipeId);

    /** Records created before the review workflow were already public: mark them validated. */
    @Modifying
    @Query("update Publication p set p.status = com.ensa.labs.recherche.bean.enums.PublicationStatus.VALIDATED where p.status is null")
    int validateLegacyPublications();
}
