package com.ensa.labs.recherche.dao;

import com.ensa.labs.recherche.bean.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EquipeRepository extends JpaRepository<Equipe, String> {
    Optional<Equipe> findByName(String name);
}
