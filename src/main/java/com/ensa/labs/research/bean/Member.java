package com.ensa.labs.research.bean;

import com.ensa.labs.research.bean.enums.MemberGrade;
import com.ensa.labs.research.bean.enums.MemberRoleInLab;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String nom;

    @Enumerated(EnumType.STRING)
    private MemberGrade grade;

    private String specialite;

    private String etablissement;

    private boolean associe;

    @Enumerated(EnumType.STRING)
    private MemberRoleInLab roleDansLaboratoire = MemberRoleInLab.MEMBER;

    @ElementCollection
    @CollectionTable(name = "member_phd_students", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "phd_student")
    private List<String> doctorantsEncadres = new ArrayList<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "lab_id")
    private Lab laboratoire;

    @ManyToMany(mappedBy = "members")
    private Set<Team> equipes = new HashSet<>();

    @Override
    public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; Member member = (Member) o; return Objects.equals(id, member.id);}    
    @Override
    public int hashCode() { return Objects.hash(id); }
}
