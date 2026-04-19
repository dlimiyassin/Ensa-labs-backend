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
@Table(name = "productions")
public class Production {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToMany
    @JoinTable(name = "production_publications", joinColumns = @JoinColumn(name = "production_id"), inverseJoinColumns = @JoinColumn(name = "publication_id"))
    private Set<Publication> publications = new HashSet<>();

    @OneToMany
    @JoinTable(name = "production_communications", joinColumns = @JoinColumn(name = "production_id"), inverseJoinColumns = @JoinColumn(name = "publication_id"))
    private Set<Publication> communications = new HashSet<>();

    @OneToMany
    @JoinTable(name = "production_theses", joinColumns = @JoinColumn(name = "production_id"), inverseJoinColumns = @JoinColumn(name = "thesis_id"))
    private Set<Thesis> theses = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "lab_id", nullable = false, unique = true)
    private Lab lab;

    @Override
    public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; Production that = (Production) o; return Objects.equals(id, that.id);}    
    @Override
    public int hashCode() { return Objects.hash(id); }
}
