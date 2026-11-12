package com.ensa.labs.recherche.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record LabDTO(
        String id,
        @NotBlank String titleFr,
        @NotBlank String titleEn,
        @NotBlank @Size(max = 50) String acronym,
        String university,
        String program,
        LocalDate accreditationStart,
        LocalDate accreditationEnd,
        String establishment,
        String phone,
        @Email String email,
        MemberDTO directeur,
        MemberDTO directeurAdjoint,
        List<MemberDTO> members,
        List<ComiteGestionMembreDTO> comiteGestion,
        List<ThematiqueRechercheDTO> thematiquesRecherche,
        List<AxeRechercheDTO> axesRecherche,
        List<EquipmentDTO> equipments,
        List<CompetenceDTO> competences,
        ProductionDTO production,
        DepartmentDTO department,
        List<EquipeDTO> equipes,
        List<TagDTO> tags
) {
}
