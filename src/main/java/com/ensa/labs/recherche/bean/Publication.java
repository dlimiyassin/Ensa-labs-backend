package com.ensa.labs.recherche.bean;

import com.ensa.labs.recherche.bean.enums.PublicationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "publications")
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 2000)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PublicationType type;

    private Integer publicationYear;

    @ElementCollection
    @CollectionTable(name = "publication_authors", joinColumns = @JoinColumn(name = "publication_id"))
    @Column(name = "author_name")
    private List<String> authors = new ArrayList<>();

    private String journal;

    private String conference;

    private String doi;

    private String pages;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lab_id")
    private Lab lab;

    @ManyToOne
    @JoinColumn(name = "equipe_id")
    private Equipe equipe;

    @Override
    public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; Publication that = (Publication) o; return Objects.equals(id, that.id);}    
    @Override
    public int hashCode() { return Objects.hash(id); }
}
