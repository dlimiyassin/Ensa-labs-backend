package com.ensa.labs.research.bean;

import com.ensa.labs.research.bean.enums.EquipmentCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "equipements")
public class Equipement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, name = "nom")
    private String nom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "categorie")
    private EquipmentCategory categorie;

    @ManyToOne(optional = false)
    @JoinColumn(name = "laboratoire_id")
    private Laboratoire laboratoire;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipement that = (Equipement) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
