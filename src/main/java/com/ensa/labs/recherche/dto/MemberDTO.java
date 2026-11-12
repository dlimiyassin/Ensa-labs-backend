package com.ensa.labs.recherche.dto;

import com.ensa.labs.recherche.bean.enums.MemberAssociationType;
import com.ensa.labs.recherche.bean.enums.MemberGrade;
import com.ensa.labs.recherche.bean.enums.MemberRoleInLab;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * @param image profile picture path relative to the upload directory (see /api/files)
 */
public record MemberDTO(
        String id,
        @NotBlank @Size(max = 255) String firstName,
        @NotBlank @Size(max = 255) String lastName,
        MemberGrade grade,
        String speciality,
        String establishment,
        MemberAssociationType associationType,
        MemberRoleInLab roleInLab,
        List<String> phdStudents,
        @NotBlank String labAcronym,
        String userId,
        String image
) {
}
