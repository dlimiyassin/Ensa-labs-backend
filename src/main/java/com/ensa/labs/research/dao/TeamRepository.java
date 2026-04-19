package com.ensa.labs.research.dao;

import com.ensa.labs.research.bean.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, String> {
    Optional<Team> findByName(String name);
}
