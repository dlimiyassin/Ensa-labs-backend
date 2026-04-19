package com.ensa.labs.research.bean;

import com.ensa.labs.research.bean.enums.ProjectStatus;
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
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @ManyToMany
    @JoinTable(name = "project_users", joinColumns = @JoinColumn(name = "project_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "lab_id")
    private Lab lab;

    @ManyToOne(optional = false)
    @JoinColumn(name = "team_id")
    private Team team;

    @Override
    public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; Project project = (Project) o; return Objects.equals(id, project.id);}    
    @Override
    public int hashCode() { return Objects.hash(id); }
}
