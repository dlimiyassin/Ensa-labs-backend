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
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "department")
    private Set<Lab> labs = new HashSet<>();

    @Override
    public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; Department that = (Department) o; return Objects.equals(id, that.id);}    
    @Override
    public int hashCode() { return Objects.hash(id); }
}
