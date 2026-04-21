package com.ensa.labs.research.dao;

import com.ensa.labs.research.bean.Laboratoire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LaboratoireRepository extends JpaRepository<Laboratoire, String> {
    Optional<Laboratoire> findByAcronyme(String acronyme);
    Optional<Laboratoire> findByTitreEn(String titreEn);
}
