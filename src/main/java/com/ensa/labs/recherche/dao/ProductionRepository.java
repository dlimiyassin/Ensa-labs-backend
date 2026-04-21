package com.ensa.labs.recherche.dao;

import com.ensa.labs.recherche.bean.Production;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductionRepository extends JpaRepository<Production, String> {
}
