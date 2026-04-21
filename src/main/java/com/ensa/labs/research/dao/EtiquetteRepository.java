package com.ensa.labs.research.dao;

import com.ensa.labs.research.bean.Etiquette;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EtiquetteRepository extends JpaRepository<Etiquette, String> {
    Optional<Etiquette> findByNom(String nom);
}
