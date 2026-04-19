package com.ensa.labs.research.dao;

import com.ensa.labs.research.bean.Thesis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThesisRepository extends JpaRepository<Thesis, String> {
}
