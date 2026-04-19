package com.ensa.labs.research.mapper;

import com.ensa.labs.research.bean.Member;
import com.ensa.labs.research.dto.MemberDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class MemberMapper {
    public MemberDTO toDto(Member member) {
        return new MemberDTO(member.getId(), member.getFirstName(), member.getLastName(), member.getGrade(), member.getSpeciality(), member.getEstablishment(),
                member.isAssociated(), member.getRoleInLab(), member.getPhdStudents(), member.getLab() != null ? member.getLab().getId() : null,
                member.getUser() != null ? member.getUser().getId() : null);
    }

    public Member toEntity(MemberDTO dto) {
        Member member = new Member();
        member.setId(dto.id());
        member.setFirstName(dto.firstName());
        member.setLastName(dto.lastName());
        member.setGrade(dto.grade());
        member.setSpeciality(dto.speciality());
        member.setEstablishment(dto.establishment());
        member.setAssociated(dto.associated());
        member.setRoleInLab(dto.roleInLab());
        member.setPhdStudents(dto.phdStudents() == null ? new ArrayList<>() : new ArrayList<>(dto.phdStudents()));
        return member;
    }
}
