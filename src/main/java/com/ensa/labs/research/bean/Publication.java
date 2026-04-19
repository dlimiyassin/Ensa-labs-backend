package com.ensa.labs.research.bean;

import com.ensa.labs.research.bean.enums.PublicationType;
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
@Table(name = "publications")
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private PublicationType type;

    private Integer publicationYear;

    @ManyToMany
    @JoinTable(name = "publication_authors", joinColumns = @JoinColumn(name = "publication_id"), inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<User> authors = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "lab_id")
    private Lab lab;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Override
    public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; Publication that = (Publication) o; return Objects.equals(id, that.id);}    
    @Override
    public int hashCode() { return Objects.hash(id); }
}
