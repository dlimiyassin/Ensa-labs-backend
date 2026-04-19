package com.ensa.labs.zBase.security.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ensa.labs.zBase.security.bean.enums.UserStatus;
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

    // personal information
    @Column(length = 100)
    private String firstName;

    @Column(length = 100)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    private String phoneNumber;

    // business logic
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

    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // helpers
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }
}