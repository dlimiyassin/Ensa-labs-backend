package com.ensa.labs.recherche.dao;

import com.ensa.labs.recherche.bean.Collaboration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollaborationRepository extends JpaRepository<Collaboration, String> {
    List<Collaboration> findByLabAcronym(String acronym);
}
