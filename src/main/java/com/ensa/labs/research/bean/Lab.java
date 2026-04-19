package com.ensa.labs.research.bean;

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
@Table(name = "labs")
public class Lab {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(unique = true)
    private String abbreviation;

    @ManyToOne(optional = false)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToMany
    @JoinTable(name = "lab_research_fields", joinColumns = @JoinColumn(name = "lab_id"), inverseJoinColumns = @JoinColumn(name = "research_field_id"))
    private Set<ResearchField> researchFields = new HashSet<>();

    @OneToMany(mappedBy = "lab")
    private Set<Team> teams = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "lab_users", joinColumns = @JoinColumn(name = "lab_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "lab_tags", joinColumns = @JoinColumn(name = "lab_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    @Override
    public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; Lab lab = (Lab) o; return Objects.equals(id, lab.id);}    
    @Override
    public int hashCode() { return Objects.hash(id); }
}
