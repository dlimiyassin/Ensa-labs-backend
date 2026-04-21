package com.ensa.labs.recherche.mapper;

import com.ensa.labs.recherche.bean.ComiteGestionMembre;
import com.ensa.labs.recherche.bean.Lab;
import com.ensa.labs.recherche.dto.LabDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class LabMapper {
    public LabDTO toDto(Lab lab) {
        return new LabDTO(
                lab.getId(),
                lab.getTitleFr(),
                lab.getTitleEn(),
                lab.getAcronym(),
                lab.getUniversity(),
                lab.getProgram(),
                lab.getAccreditationStart(),
                lab.getAccreditationEnd(),
                lab.getEstablishment(),
                lab.getPhone(),
                lab.getEmail(),
                lab.getDirecteur() != null ? lab.getDirecteur().getId() : null,
                lab.getDirecteurAdjoint() != null ? lab.getDirecteurAdjoint().getId() : null,
                lab.getMembers().stream().map(m -> m.getId()).toList(),
                lab.getComiteGestion().stream().map(ComiteGestionMembre::getNomEnseignant).toList(),
                lab.getDomainesRecherche().stream().map(r -> r.getName()).toList(),
                lab.getAxesRecherche().stream().map(r -> r.getTitle()).toList(),
                lab.getEquipments().stream().map(e -> e.getId()).toList(),
                lab.getCompetences().stream().map(c -> c.getId()).toList(),
                lab.getProduction() != null ? lab.getProduction().getId() : null,
                lab.getDepartment() != null ? lab.getDepartment().getName() : null,
                lab.getEquipes().stream().map(t -> t.getId()).toList(),
                lab.getTags().stream().map(t -> t.getName()).toList()
        );
    }

    public Lab toEntity(LabDTO dto) {
        Lab lab = new Lab();
        lab.setId(dto.id());
        lab.setTitleFr(dto.titleFr());
        lab.setTitleEn(dto.titleEn());
        lab.setAcronym(dto.acronym());
        lab.setUniversity(dto.university());
        lab.setProgram(dto.program());
        lab.setAccreditationStart(dto.accreditationStart());
        lab.setAccreditationEnd(dto.accreditationEnd());
        lab.setEstablishment(dto.establishment());
        lab.setPhone(dto.phone());
        lab.setEmail(dto.email());
        lab.setComiteGestion(new ArrayList<>());
        return lab;
    }
}
