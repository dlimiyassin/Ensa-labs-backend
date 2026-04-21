package com.ensa.labs.research.bean;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "theses_rename")
public class These {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, name = "auteur")
    private String auteur;

    @Column(nullable = false, length = 2000, name = "titre")
    private String titre;

    @Column(name = "date_soutenance")
    private LocalDate dateSoutenance;

    @Column(name = "directeur_these")
    private String directeurThese;

    @ManyToOne(optional = false)
    @JoinColumn(name = "laboratoire_id")
    private Laboratoire laboratoire;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        These that = (These) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
