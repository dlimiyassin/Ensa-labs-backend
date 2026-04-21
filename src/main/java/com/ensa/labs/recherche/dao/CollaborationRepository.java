package com.ensa.labs.recherche.dao;

import com.ensa.labs.recherche.bean.Collaboration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollaborationRepository extends JpaRepository<Collaboration, String> {
}
