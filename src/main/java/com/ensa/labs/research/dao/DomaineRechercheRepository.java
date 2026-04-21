package com.ensa.labs.research.dao;

import com.ensa.labs.research.bean.DomaineRecherche;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DomaineRechercheRepository extends JpaRepository<DomaineRecherche, String> {
    Optional<DomaineRecherche> findByNom(String nom);
}
