package com.ensa.labs.research.dao;

import com.ensa.labs.research.bean.ResearchItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResearchItemRepository extends JpaRepository<ResearchItem, String> {
}
