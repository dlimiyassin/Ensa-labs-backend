package com.ensa.labs.research.dao;

import com.ensa.labs.research.bean.Equipement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipementRepository extends JpaRepository<Equipement, String> {
}
