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
@Table(name = "equipes")
public class Equipe {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lab_id")
    private Lab lab;

    @OneToMany(mappedBy = "equipe")
    private Set<AxeRecherche> axesRecherche = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "equipe_members", joinColumns = @JoinColumn(name = "equipe_id"), inverseJoinColumns = @JoinColumn(name = "member_id"))
    private Set<Member> members = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "responsable_id")
    private Member responsable;

    @Override
    public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; Equipe equipe = (Equipe) o; return Objects.equals(id, equipe.id);}
    @Override
    public int hashCode() { return Objects.hash(id); }
}
