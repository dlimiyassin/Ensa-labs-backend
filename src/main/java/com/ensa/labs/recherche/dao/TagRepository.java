package com.ensa.labs.recherche.dao;

import com.ensa.labs.recherche.bean.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, String> {
    Optional<Tag> findByName(String name);
}
