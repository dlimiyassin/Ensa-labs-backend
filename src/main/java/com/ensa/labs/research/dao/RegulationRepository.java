package com.ensa.labs.research.dao;

import com.ensa.labs.research.bean.Regulation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegulationRepository extends JpaRepository<Regulation, String> {
}
