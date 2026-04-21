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
@Table(name = "productions_rename")
public class Production_Renamed {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToMany
    @JoinTable(name = "production_publications_rename", joinColumns = @JoinColumn(name = "production_id"), inverseJoinColumns = @JoinColumn(name = "publication_id"))
    private Set<Publication_Renamed> publications = new HashSet<>();

    @OneToMany
    @JoinTable(name = "production_communications_rename", joinColumns = @JoinColumn(name = "production_id"), inverseJoinColumns = @JoinColumn(name = "publication_id"))
    private Set<Publication_Renamed> communications = new HashSet<>();

    @OneToMany
    @JoinTable(name = "production_theses_rename", joinColumns = @JoinColumn(name = "production_id"), inverseJoinColumns = @JoinColumn(name = "these_id"))
    private Set<These> theses = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "laboratoire_id", nullable = false, unique = true)
    private Laboratoire laboratoire;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Production_Renamed that = (Production_Renamed) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
