package com.ensa.labs.recherche.bean;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class ComiteGestionMembre {
    @Column(name = "nom_enseignant")
    private String nomEnseignant;

    @Column(name = "role_comite", length = 1500)
    private String roleComite;

    /** Lab member behind this committee seat (gives access to id and image). */
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
