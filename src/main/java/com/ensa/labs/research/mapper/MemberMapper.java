package com.ensa.labs.research.mapper;

import com.ensa.labs.research.bean.Member;
import com.ensa.labs.research.dto.MemberDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class MemberMapper {
    public MemberDTO toDto(Member member) {
        return new MemberDTO(member.getId(), member.getPrenom(), member.getNom(), member.getGrade(), member.getSpecialite(), member.getEtablissement(),
                member.isAssocie(), member.getRoleDansLaboratoire(), member.getDoctorantsEncadres(), member.getLaboratoire() != null ? member.getLaboratoire().getId() : null);
    }

    public Member toEntity(MemberDTO dto) {
        Member member = new Member();
        member.setId(dto.id());
        member.setPrenom(dto.prenom());
        member.setNom(dto.nom());
        member.setGrade(dto.grade());
        member.setSpecialite(dto.specialite());
        member.setEtablissement(dto.etablissement());
        member.setAssocie(dto.associe());
        member.setRoleDansLaboratoire(dto.roleDansLaboratoire());
        member.setDoctorantsEncadres(dto.doctorantsEncadres() == null ? new ArrayList<>() : new ArrayList<>(dto.doctorantsEncadres()));
        return member;
    }
}
