package com.ensa.labs.recherche.service;

import com.ensa.labs.exception.ResourceNotFoundException;
import com.ensa.labs.recherche.bean.Member;
import com.ensa.labs.recherche.dao.LabRepository;
import com.ensa.labs.recherche.dao.MemberRepository;
import com.ensa.labs.recherche.dto.MemberDTO;
import com.ensa.labs.recherche.mapper.MemberMapper;
import com.ensa.labs.zBase.security.dao.facade.UserDao;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final LabRepository labRepository;
    private final UserDao userDao;
    private final MemberMapper memberMapper;

    public MemberService(MemberRepository memberRepository, LabRepository labRepository, UserDao userDao, MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.labRepository = labRepository;
        this.userDao = userDao;
        this.memberMapper = memberMapper;
    }

    public List<MemberDTO> findAll() { return memberRepository.findAll().stream().map(memberMapper::toDto).toList(); }
    public MemberDTO findById(String id) { return memberMapper.toDto(get(id)); }
    public List<MemberDTO> findByLabAcronym(String acronym) { return memberRepository.findByLabAcronym(acronym).stream().map(memberMapper::toDto).toList(); }

    public MemberDTO create(MemberDTO dto) {
        Member member = memberMapper.toEntity(dto);
        applyRelations(member, dto);
        return memberMapper.toDto(memberRepository.save(member));
    }

    public MemberDTO update(String id, MemberDTO dto) {
        Member member = get(id);
        member.setFirstName(dto.firstName());
        member.setLastName(dto.lastName());
        member.setGrade(dto.grade());
        member.setSpeciality(dto.speciality());
        member.setEstablishment(dto.establishment());
        member.setAssociationType(dto.associationType());
        member.setRoleInLab(dto.roleInLab());
        member.setPhdStudents(dto.phdStudents() == null ? new ArrayList<>() : new ArrayList<>(dto.phdStudents()));
        applyRelations(member, dto);
        return memberMapper.toDto(memberRepository.save(member));
    }

    public void delete(String id) { memberRepository.delete(get(id)); }

    private void applyRelations(Member member, MemberDTO dto) {
        member.setLab(labRepository.findByAcronym(dto.labAcronym()).orElseThrow(() -> new ResourceNotFoundException("Lab", "acronym", dto.labAcronym())));
        member.setUser(dto.userId() == null ? null : userDao.findById(dto.userId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", dto.userId())));
    }

    private Member get(String id) { return memberRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Member", "id", id)); }
}
