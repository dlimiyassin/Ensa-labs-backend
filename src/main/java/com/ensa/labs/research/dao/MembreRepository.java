package com.ensa.labs.research.dao;

import com.ensa.labs.research.bean.Membre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembreRepository extends JpaRepository<Membre, String> {
    Optional<Membre> findByLaboratoireIdAndUtilisateurId(String laboratoireId, String utilisateurId);
}
