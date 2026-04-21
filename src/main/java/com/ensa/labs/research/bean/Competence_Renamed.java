package com.ensa.labs.research.bean;

import com.ensa.labs.research.bean.enums.CompetenceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "competences_rename")
public class Competence_Renamed {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 1500, name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "type")
    private CompetenceType type;

    @ManyToOne(optional = false)
    @JoinColumn(name = "laboratoire_id")
    private Laboratoire laboratoire;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Competence_Renamed that = (Competence_Renamed) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
