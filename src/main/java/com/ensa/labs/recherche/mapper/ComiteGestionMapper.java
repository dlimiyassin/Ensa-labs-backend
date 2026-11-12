package com.ensa.labs.recherche.mapper;

import com.ensa.labs.recherche.bean.ComiteGestionMembre;
import com.ensa.labs.recherche.dto.ComiteGestionMembreDTO;
import org.springframework.stereotype.Component;

@Component
public class ComiteGestionMapper {
    public ComiteGestionMembreDTO toDto(ComiteGestionMembre comiteGestionMembre) {
        var member = comiteGestionMembre.getMember();
        return new ComiteGestionMembreDTO(
                comiteGestionMembre.getNomEnseignant(),
                comiteGestionMembre.getRoleComite(),
                member != null ? member.getId() : null,
                member != null ? member.getImage() : null);
    }

    /** Member link is resolved by the service (it needs the repository). */
    public ComiteGestionMembre toEntity(ComiteGestionMembreDTO dto) {
        ComiteGestionMembre comiteGestionMembre = new ComiteGestionMembre();
        comiteGestionMembre.setNomEnseignant(dto.nomEnseignant());
        comiteGestionMembre.setRoleComite(dto.roleComite());
        return comiteGestionMembre;
    }
}
