package com.ensa.labs.recherche.dao;

import com.ensa.labs.recherche.bean.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, String> {
}
