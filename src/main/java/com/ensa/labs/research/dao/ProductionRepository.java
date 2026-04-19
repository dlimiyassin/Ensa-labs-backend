package com.ensa.labs.research.dao;

import com.ensa.labs.research.bean.Production;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductionRepository extends JpaRepository<Production, String> {
}
