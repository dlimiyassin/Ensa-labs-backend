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
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String nom;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lab_id")
    private Lab laboratoire;

    @ManyToOne(optional = false)
    @JoinColumn(name = "research_field_id")
    private ResearchField domaineRecherche;

    @ManyToMany
    @JoinTable(name = "team_members", joinColumns = @JoinColumn(name = "team_id"), inverseJoinColumns = @JoinColumn(name = "member_id"))
    private Set<Member> members = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "leader_id")
    private Member chefEquipe;

    @Override
    public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; Team team = (Team) o; return Objects.equals(id, team.id);}    
    @Override
    public int hashCode() { return Objects.hash(id); }
}
