package com.ensa.labs.research.bean;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
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
}
