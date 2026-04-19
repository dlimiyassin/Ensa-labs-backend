package com.ensa.labs.research.bean;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<Lab> labs = new HashSet<>();

    @Override
    public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; Tag tag = (Tag) o; return Objects.equals(id, tag.id);}    
    @Override
    public int hashCode() { return Objects.hash(id); }
}
