package com.ensa.labs.research.dao;

import com.ensa.labs.research.bean.Departement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartementRepository extends JpaRepository<Departement, String> {
    Optional<Departement> findByNom(String nom);
}
