package com.ensa.labs.recherche.dao;

import java.util.List;
import com.ensa.labs.recherche.bean.Production;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductionRepository extends JpaRepository<Production, String> {
    Optional<Production> findByLabAcronym(String acronym);
    List<Production> findByPublicationsId(String publicationId);
    List<Production> findByCommunicationsId(String publicationId);
    List<Production> findByThesesId(String thesisId);
}
