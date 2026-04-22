package com.ensa.labs.recherche.dao;

import com.ensa.labs.recherche.bean.Publication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicationRepository extends JpaRepository<Publication, String> {
    List<Publication> findByLabAcronym(String acronym);
}
