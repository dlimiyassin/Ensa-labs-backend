package com.ensa.labs.research.dao;

import com.ensa.labs.research.bean.Publication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicationRepository extends JpaRepository<Publication, String> {
}
