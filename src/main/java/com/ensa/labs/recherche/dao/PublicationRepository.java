package com.ensa.labs.recherche.dao;

import com.ensa.labs.recherche.bean.Publication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicationRepository extends JpaRepository<Publication, String> {
}
