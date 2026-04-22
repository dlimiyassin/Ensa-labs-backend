package com.ensa.labs.recherche.dao;

import com.ensa.labs.recherche.bean.Lab;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LabRepository extends JpaRepository<Lab, String> {
    Optional<Lab> findByAcronym(String acronym);
    Optional<Lab> findByTitleEn(String titleEn);
    List<Lab> findByEstablishmentIgnoreCase(String establishment);
    List<Lab> findByDepartmentNameIgnoreCase(String departmentName);
}
