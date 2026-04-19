package com.ensa.labs.research.dao;

import com.ensa.labs.research.bean.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, String> {
}
