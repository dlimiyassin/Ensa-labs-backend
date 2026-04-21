package com.ensa.labs.recherche.dao;

import com.ensa.labs.recherche.bean.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, String> {
    Optional<Department> findByName(String name);
}
