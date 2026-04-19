package com.ensa.labs.research.dto;

import com.ensa.labs.research.bean.enums.MemberGrade;
import com.ensa.labs.research.bean.enums.MemberRoleInLab;

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
