package com.ensa.labs.research.bean;

import com.ensa.labs.zBase.security.bean.Utilisateur;
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

    @Column(nullable = false, name = "nom")
    private String nom;

    @ManyToOne(optional = false)
    @JoinColumn(name = "laboratoire_id")
    private Laboratoire laboratoire;

    @ManyToOne(optional = false)
    @JoinColumn(name = "domaine_recherche_id")
    private DomaineRecherche domaineRecherche;

    @ManyToMany
    @JoinTable(name = "equipe_membres", joinColumns = @JoinColumn(name = "equipe_id"), inverseJoinColumns = @JoinColumn(name = "utilisateur_id"))
    private Set<Utilisateur> membres = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "chef_equipe_id")
    private Utilisateur chefEquipe;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipe equipe = (Equipe) o;
        return Objects.equals(id, equipe.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
