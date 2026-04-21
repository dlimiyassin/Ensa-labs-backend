package com.ensa.labs.recherche.dao;

import com.ensa.labs.recherche.bean.Thesis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThesisRepository extends JpaRepository<Thesis, String> {
}
