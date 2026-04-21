package com.ensa.labs.recherche.bean;

import com.ensa.labs.recherche.bean.enums.EquipmentCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "equipments")
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipmentCategory category;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lab_id")
    private Lab lab;

    @Override
    public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; Equipment that = (Equipment) o; return Objects.equals(id, that.id);}    
    @Override
    public int hashCode() { return Objects.hash(id); }
}
