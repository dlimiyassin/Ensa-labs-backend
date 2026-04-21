package com.ensa.labs.research.bean;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "laboratoires")
public class Laboratoire {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, name = "titre_fr")
    private String titreFr;

    @Column(nullable = false, name = "titre_en")
    private String titreEn;

    @Column(nullable = false, unique = true, name = "acronyme")
    private String acronyme;

    @Column(name = "universite")
    private String universite;

    @Column(name = "programme")
    private String programme;

    @Column(name = "date_debut_acreditation")
    private LocalDate dateDebutAccreditation;

    @Column(name = "date_fin_accreditation")
    private LocalDate dateFinAccreditation;

    @Column(name = "etablissement")
    private String etablissement;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "courrier_electronique")
    private String courrierElectronique;

    @ManyToOne
    @JoinColumn(name = "directeur_id")
    private Membre directeur;

    @ManyToOne
    @JoinColumn(name = "directeur_adjoint_id")
    private Membre directeurAdjoint;

    @OneToMany(mappedBy = "laboratoire")
    private Set<Membre> membres = new HashSet<>();

    // Thématiques de recherche
    @ManyToMany
    @JoinTable(name = "laboratoire_domaines_recherche", joinColumns = @JoinColumn(name = "laboratoire_id"), inverseJoinColumns = @JoinColumn(name = "domaine_recherche_id"))
    private Set<DomaineRecherche> domainesRecherche = new HashSet<>();

    // Axes de recherche
    @OneToMany(mappedBy = "laboratoire")
    private Set<ElementRecherche> elementsRecherche = new HashSet<>();

    @OneToMany(mappedBy = "laboratoire")
    private Set<Equipement> equipements = new HashSet<>();

    @OneToMany(mappedBy = "laboratoire")
    private Set<Competence> competences = new HashSet<>();

    @OneToOne(mappedBy = "laboratoire", cascade = CascadeType.ALL)
    private Production production;

    @ElementCollection
    @CollectionTable(name = "laboratoire_comite_gestion", joinColumns = @JoinColumn(name = "laboratoire_id"))
    private List<ComiteGestionMembre> comiteGestion = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "departement_id")
    private Departement departement;

    @OneToMany(mappedBy = "laboratoire")
    private Set<Equipe> equipes = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "laboratoire_etiquettes", joinColumns = @JoinColumn(name = "laboratoire_id"), inverseJoinColumns = @JoinColumn(name = "etiquette_id"))
    private Set<Etiquette> etiquettes = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Laboratoire laboratoire = (Laboratoire) o;
        return Objects.equals(id, laboratoire.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
