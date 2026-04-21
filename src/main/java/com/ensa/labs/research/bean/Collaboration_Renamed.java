package com.ensa.labs.research.bean;

import com.ensa.labs.research.bean.enums.CollaborationScope;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "collaborations_rename")
public class Collaboration_Renamed {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, name = "organisation")
    private String organisation;

    @Column(name = "etablissement")
    private String etablissement;

    @Column(name = "theme")
    private String theme;

    @Column(name = "nature")
    private String nature;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "portee")
    private CollaborationScope portee;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Collaboration_Renamed that = (Collaboration_Renamed) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
