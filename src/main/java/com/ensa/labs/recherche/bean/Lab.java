package com.ensa.labs.recherche.bean;

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
@Table(name = "labs")
public class Lab {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String titleFr;

    @Column(nullable = false)
    private String titleEn;

    @Column(nullable = false, unique = true)
    private String acronym;

    private String university;
    private String program;
    private LocalDate accreditationStart;
    private LocalDate accreditationEnd;
    private String establishment;
    private String phone;
    private String email;

    @ManyToOne
    @JoinColumn(name = "directeur_id")
    private Member directeur;

    @ManyToOne
    @JoinColumn(name = "directeur_adjoint_id")
    private Member directeurAdjoint;

    @OneToMany(mappedBy = "lab")
    private Set<Member> members = new HashSet<>();

    // Thématiques de recherche
    @ManyToMany
    @JoinTable(name = "lab_domaines_recherche", joinColumns = @JoinColumn(name = "lab_id"), inverseJoinColumns = @JoinColumn(name = "domaine_recherche_id"))
    private Set<DomaineRecherche> domainesRecherche = new HashSet<>();

    // Axes de recherche
    @OneToMany(mappedBy = "lab")
    private Set<AxeRecherche> axesRecherche = new HashSet<>();

    @OneToMany(mappedBy = "lab")
    private Set<Equipment> equipments = new HashSet<>();

    @OneToMany(mappedBy = "lab")
    private Set<Competence> competences = new HashSet<>();

    @OneToOne(mappedBy = "lab", cascade = CascadeType.ALL)
    private Production production;

    @ElementCollection
    @CollectionTable(name = "lab_comite_gestion", joinColumns = @JoinColumn(name = "lab_id"))
    private List<ComiteGestionMembre> comiteGestion = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "lab")
    private Set<Equipe> equipes = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "lab_tags", joinColumns = @JoinColumn(name = "lab_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "lab")
    private Set<Collaboration> collaborations = new HashSet<>();

    @Override
    public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; Lab lab = (Lab) o; return Objects.equals(id, lab.id);}    
    @Override
    public int hashCode() { return Objects.hash(id); }
}
