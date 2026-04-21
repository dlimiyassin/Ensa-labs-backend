package com.ensa.labs.recherche.dto;

import com.ensa.labs.recherche.bean.enums.MemberGrade;
import com.ensa.labs.recherche.bean.enums.MemberRoleInLab;

import java.util.List;

public record MemberDTO(
        String id,
        String firstName,
        String lastName,
        MemberGrade grade,
        String speciality,
        String establishment,
        boolean associated,
        MemberRoleInLab roleInLab,
        List<String> phdStudents,
        String labId,
        String userId
) {
}
