package com.ensa.labs.zBase.db.data;

import com.ensa.labs.recherche.bean.enums.MemberRoleInLab;

public record MemberData(
        String firstName,
        String lastName,
        String grade,
        String speciality,
        String establishment,
        MemberRoleInLab memberRoleInLab
) {}
