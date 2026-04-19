package com.ensa.labs.research.dao;

import com.ensa.labs.research.bean.Lab;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LabRepository extends JpaRepository<Lab, String> {
    Optional<Lab> findByAcronym(String acronym);
    Optional<Lab> findByTitleEn(String titleEn);
}
