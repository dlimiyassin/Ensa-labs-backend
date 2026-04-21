package com.ensa.labs.recherche.bean;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "axes_recherche")
public class AxeRecherche {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 1000)
    private String title;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lab_id")
    private Lab lab;

    @Override
    public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; AxeRecherche that = (AxeRecherche) o; return Objects.equals(id, that.id);}
    @Override
    public int hashCode() { return Objects.hash(id); }
}
