package com.ensa.labs.recherche.bean;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "domaines_recherche")
public class DomaineRecherche {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "domainesRecherche")
    private Set<Lab> labs = new HashSet<>();

    @Override
    public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; DomaineRecherche that = (DomaineRecherche) o; return Objects.equals(id, that.id);}
    @Override
    public int hashCode() { return Objects.hash(id); }
}
