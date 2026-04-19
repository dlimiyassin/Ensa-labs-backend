package com.ensa.labs.research.bean;

import com.ensa.labs.zBase.security.bean.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
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
    @JoinColumn(name = "director_id")
    private Member director;

    @ManyToOne
    @JoinColumn(name = "deputy_director_id")
    private Member deputyDirector;

    @OneToMany(mappedBy = "lab")
    private Set<Member> members = new HashSet<>();

    @OneToMany(mappedBy = "lab")
    private Set<ResearchItem> researchItems = new HashSet<>();

    @OneToMany(mappedBy = "lab")
    private Set<Equipment> equipments = new HashSet<>();

    @OneToMany(mappedBy = "lab")
    private Set<Competence> competences = new HashSet<>();

    @OneToMany(mappedBy = "lab")
    private Set<Collaboration> collaborations = new HashSet<>();

    @OneToOne(mappedBy = "lab", cascade = CascadeType.ALL)
    private Production production;

    @OneToMany(mappedBy = "lab")
    private Set<Regulation> regulations = new HashSet<>();

    // Keep existing relations for backward compatibility.
    @ManyToOne
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
