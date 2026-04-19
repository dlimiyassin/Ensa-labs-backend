package com.ensa.labs.zBase.security.bean;

import com.ensa.labs.research.bean.Lab;
import com.ensa.labs.research.bean.Team;
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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false, length = 80)
    private String username;

    @Column(unique = true, length = 150)
    private String email;

    @Column(length = 30)
    private String cin;

    @Column(length = 100)
    private String firstName;

    @Column(length = 100)
    private String lastName;

    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    private boolean enabled = false;

    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.EN_ATTENTE;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant lastLogin;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(mappedBy = "members")
    private Set<Team> teams = new HashSet<>();

    @ManyToMany(mappedBy = "users")
    private Set<Lab> labs = new HashSet<>();

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
