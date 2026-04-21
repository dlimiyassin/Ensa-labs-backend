package com.ensa.labs.recherche.bean;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "theses")
public class Thesis {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, length = 2000)
    private String title;

    private LocalDate defenseDate;

    private String supervisor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lab_id")
    private Lab lab;

    @Override
    public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; Thesis that = (Thesis) o; return Objects.equals(id, that.id);}    
    @Override
    public int hashCode() { return Objects.hash(id); }
}
