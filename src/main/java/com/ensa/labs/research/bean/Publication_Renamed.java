package com.ensa.labs.research.bean;

import com.ensa.labs.research.bean.enums.PublicationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "publications_rename")
public class Publication_Renamed {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 2000, name = "titre")
    private String titre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "type")
    private PublicationType type;

    @Column(name = "annee_publication")
    private Integer anneePublication;

    @ElementCollection
    @CollectionTable(name = "publication_auteurs_rename", joinColumns = @JoinColumn(name = "publication_id"))
    @Column(name = "nom_auteur")
    private List<String> auteurs = new ArrayList<>();

    @Column(name = "journal")
    private String journal;

    @Column(name = "conference")
    private String conference;

    @Column(name = "doi")
    private String doi;

    @Column(name = "pages")
    private String pages;

    @ManyToOne(optional = false)
    @JoinColumn(name = "laboratoire_id")
    private Laboratoire laboratoire;

    @ManyToOne
    @JoinColumn(name = "equipe_id")
    private Equipe equipe;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publication_Renamed that = (Publication_Renamed) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
