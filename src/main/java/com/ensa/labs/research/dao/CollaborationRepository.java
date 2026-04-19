package com.ensa.labs.research.dao;

import com.ensa.labs.research.bean.Collaboration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollaborationRepository extends JpaRepository<Collaboration, String> {
}
