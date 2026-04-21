package com.ensa.labs.recherche.dao;

import com.ensa.labs.recherche.bean.DomaineRecherche;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DomaineRechercheRepository extends JpaRepository<DomaineRecherche, String> {
    Optional<DomaineRecherche> findByName(String name);
}
