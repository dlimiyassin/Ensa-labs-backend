package com.ensa.labs.research.bean;

import com.ensa.labs.research.bean.enums.MemberGrade;
import com.ensa.labs.research.bean.enums.MemberRoleInLab;
import com.ensa.labs.zBase.security.bean.Utilisateur;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "membres")
public class Membre {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "grade")
    private MemberGrade grade;

    @Column(name = "specialite")
    private String specialite;

    @Column(name = "etablissement")
    private String etablissement;

    @Column(name = "associe")
    private boolean associe;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_dans_laboratoire")
    private MemberRoleInLab roleDansLaboratoire = MemberRoleInLab.MEMBER;

    @ElementCollection
    @CollectionTable(name = "membre_doctorants_encadres", joinColumns = @JoinColumn(name = "membre_id"))
    @Column(name = "doctorant_encadre")
    private List<String> doctorantsEncadres = new ArrayList<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "laboratoire_id")
    private Laboratoire laboratoire;

    @OneToOne(optional = false)
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Membre membre = (Membre) o;
        return Objects.equals(id, membre.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
