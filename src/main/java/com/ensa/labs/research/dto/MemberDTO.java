package com.ensa.labs.research.dto;

import com.ensa.labs.research.bean.enums.MemberGrade;
import com.ensa.labs.research.bean.enums.MemberRoleInLab;

import java.util.List;

public record MemberDTO(
        String id,
        String prenom,
        String nom,
        MemberGrade grade,
        String specialite,
        String etablissement,
        boolean associe,
        MemberRoleInLab roleDansLaboratoire,
        List<String> doctorantsEncadres,
        String laboratoireId
) {
}
