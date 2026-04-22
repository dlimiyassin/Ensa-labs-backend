package com.ensa.labs.recherche.dto;

import java.time.LocalDate;
import java.util.List;

public record LabDTO(
        String id,
        String titleFr,
        String titleEn,
        String acronym,
        String university,
        String program,
        LocalDate accreditationStart,
        LocalDate accreditationEnd,
        String establishment,
        String phone,
        String email,
        MemberDTO directeur,
        MemberDTO directeurAdjoint,
        List<MemberDTO> members,
        List<ComiteGestionMembreDTO> comiteGestion,
        List<DomaineRechercheDTO> domainesRecherche,
        List<AxeRechercheDTO> axesRecherche,
        List<EquipmentDTO> equipments,
        List<CompetenceDTO> competences,
        ProductionDTO production,
        DepartmentDTO department,
        List<EquipeDTO> equipes,
        List<TagDTO> tags
) {
}
