package com.ensa.labs.recherche.dao;

import java.util.List;
import com.ensa.labs.recherche.bean.Competence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetenceRepository extends JpaRepository<Competence, String> {
    List<Competence> findByLabAcronym(String acronym);
}
