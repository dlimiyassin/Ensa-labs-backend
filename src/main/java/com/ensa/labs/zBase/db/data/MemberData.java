package com.ensa.labs.zBase.db.data;

import com.ensa.labs.recherche.bean.enums.MemberRoleInLab;
import com.ensa.labs.recherche.bean.enums.MemberAssociationType;

import java.util.List;

public record MemberData(
        String firstName,
        String lastName,
        String grade,
        String speciality,
        String establishment,
        MemberRoleInLab memberRoleInLab,
        MemberAssociationType associationType,
        List<String> phdStudents,
        String labAcronym
) {
    public MemberData(String firstName, String lastName, String grade, String speciality, String establishment, MemberRoleInLab memberRoleInLab) {
        this(firstName, lastName, grade, speciality, establishment, memberRoleInLab, MemberAssociationType.PERMENANET, List.of(), null);
    }
}
