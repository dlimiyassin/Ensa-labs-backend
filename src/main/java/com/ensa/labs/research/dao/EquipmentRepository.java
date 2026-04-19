package com.ensa.labs.research.dao;

import com.ensa.labs.research.bean.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, String> {
}
