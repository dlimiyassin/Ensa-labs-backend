package com.ensa.labs.research.dao;

import com.ensa.labs.research.bean.ResearchField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResearchFieldRepository extends JpaRepository<ResearchField, String> {
    Optional<ResearchField> findByName(String name);
}
