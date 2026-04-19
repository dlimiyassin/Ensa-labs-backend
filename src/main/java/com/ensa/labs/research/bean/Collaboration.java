package com.ensa.labs.research.bean;

import com.ensa.labs.research.bean.enums.CollaborationScope;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "collaborations")
public class Collaboration {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String organization;

    private String establishment;

    private String theme;

    private String nature;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CollaborationScope scope;

    @Override
    public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; Collaboration that = (Collaboration) o; return Objects.equals(id, that.id);}    
    @Override
    public int hashCode() { return Objects.hash(id); }
}
