package com.ensa.labs.recherche.dao;

import java.util.List;
import com.ensa.labs.recherche.bean.AxeRecherche;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AxeRechercheRepository extends JpaRepository<AxeRecherche, String> {
    List<AxeRecherche> findByEquipeId(String equipeId);
}
