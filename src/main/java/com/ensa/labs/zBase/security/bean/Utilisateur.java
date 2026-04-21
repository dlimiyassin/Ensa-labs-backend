package com.ensa.labs.zBase.security.bean;

import com.ensa.labs.research.bean.Equipe;
import com.ensa.labs.zBase.security.bean.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "utilisateurs")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false, length = 80, name = "nom_utilisateur")
    private String nomUtilisateur;

    @Column(unique = true, length = 150, name = "courrier_electronique")
    private String courrierElectronique;

    @Column(length = 30, name = "cin")
    private String cin;

    @Column(length = 100, name = "prenom")
    private String prenom;

    @Column(length = 100, name = "nom")
    private String nom;

    @Column(name = "numero_telephone")
    private String numeroTelephone;

    @Column(nullable = false, name = "mot_de_passe")
    private String motDePasse;

    @Column(name = "actif")
    private boolean actif = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private UserStatus statut = UserStatus.EN_ATTENTE;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "derniere_connexion")
    private Instant derniereConnexion;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "utilisateur_roles",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(mappedBy = "membres")
    private Set<Equipe> equipes = new HashSet<>();

    public Utilisateur() {
    }

    public Utilisateur(String nomUtilisateur, String motDePasse) {
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilisateur utilisateur = (Utilisateur) o;
        return Objects.equals(id, utilisateur.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
