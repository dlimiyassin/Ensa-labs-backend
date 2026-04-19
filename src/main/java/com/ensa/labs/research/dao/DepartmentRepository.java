package com.ensa.labs.research.dao;

import com.ensa.labs.research.bean.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, String> {
    Optional<Department> findByName(String name);
}
