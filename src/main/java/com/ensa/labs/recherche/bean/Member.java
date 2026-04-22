package com.ensa.labs.recherche.bean;

import com.ensa.labs.recherche.bean.enums.MemberAssociationType;
import com.ensa.labs.recherche.bean.enums.MemberGrade;
import com.ensa.labs.recherche.bean.enums.MemberRoleInLab;
import com.ensa.labs.zBase.security.bean.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    private MemberGrade grade;

    private String speciality;

    private String establishment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberAssociationType associationType = MemberAssociationType.PERMENANET;

    @Enumerated(EnumType.STRING)
    private MemberRoleInLab roleInLab = MemberRoleInLab.MEMBER;

    @ElementCollection
    @CollectionTable(name = "member_phd_students", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "phd_student")
    private List<String> phdStudents = new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "lab_id")
    private Lab lab;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; Member member = (Member) o; return Objects.equals(id, member.id);}    
    @Override
    public int hashCode() { return Objects.hash(id); }
}
