package com.ensa.labs.recherche.bean;

import com.ensa.labs.zBase.security.bean.User;
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "domaine_recherche_id")
    private DomaineRecherche domaineRecherche;

    @ManyToMany
    @JoinTable(name = "equipe_members", joinColumns = @JoinColumn(name = "equipe_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> members = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "responsable_id")
    private User responsable;

    @Override
    public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; Equipe equipe = (Equipe) o; return Objects.equals(id, equipe.id);}
    @Override
    public int hashCode() { return Objects.hash(id); }
}
