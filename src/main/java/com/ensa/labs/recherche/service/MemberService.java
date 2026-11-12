package com.ensa.labs.recherche.service;

import com.ensa.labs.exception.ResourceNotFoundException;
import com.ensa.labs.recherche.bean.Member;
import com.ensa.labs.recherche.dao.EquipeRepository;
import com.ensa.labs.recherche.dao.LabRepository;
import com.ensa.labs.recherche.dao.MemberRepository;
import com.ensa.labs.recherche.dto.MemberDTO;
import com.ensa.labs.recherche.mapper.MemberMapper;
import com.ensa.labs.zBase.security.dao.facade.UserDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final LabRepository labRepository;
    private final EquipeRepository equipeRepository;
    private final UserDao userDao;
    private final MemberMapper memberMapper;
    private final AccessControlService accessControlService;

    public MemberService(MemberRepository memberRepository, LabRepository labRepository, EquipeRepository equipeRepository, UserDao userDao, MemberMapper memberMapper, AccessControlService accessControlService) {
        this.memberRepository = memberRepository;
        this.labRepository = labRepository;
        this.equipeRepository = equipeRepository;
        this.userDao = userDao;
        this.memberMapper = memberMapper;
        this.accessControlService = accessControlService;
    }

    public List<MemberDTO> findAll() { return memberRepository.findAll().stream().map(memberMapper::toDto).toList(); }

    public MemberDTO findById(String id) { return memberMapper.toDto(get(id)); }

    public List<MemberDTO> findByLabAcronym(String acronym) { return memberRepository.findByLabAcronym(acronym).stream().map(memberMapper::toDto).toList(); }

    @Transactional
    public MemberDTO create(MemberDTO dto) {
        accessControlService.assertCanManageLab(dto.labAcronym());
        Member member = memberMapper.toEntity(dto);
        member.setId(null);
        applyRelations(member, dto);
        return memberMapper.toDto(memberRepository.save(member));
    }

    @Transactional
    public MemberDTO update(String id, MemberDTO dto) {
        Member member = get(id);
        accessControlService.assertCanManageLab(member.getLab() != null ? member.getLab().getAcronym() : null);
        accessControlService.assertCanManageLab(dto.labAcronym());
        member.setFirstName(dto.firstName());
        member.setLastName(dto.lastName());
        member.setGrade(dto.grade());
        member.setSpeciality(dto.speciality());
        member.setEstablishment(dto.establishment());
        if (dto.associationType() != null) member.setAssociationType(dto.associationType());
        if (dto.roleInLab() != null) member.setRoleInLab(dto.roleInLab());
        member.setImage(dto.image());
        member.setPhdStudents(dto.phdStudents() == null ? new ArrayList<>() : new ArrayList<>(dto.phdStudents()));
        applyRelations(member, dto);
        return memberMapper.toDto(memberRepository.save(member));
    }

    /** Removes the member and every reference to it (lab direction, committee, teams). */
    @Transactional
    public void delete(String id) {
        Member member = get(id);
        accessControlService.assertCanManageLab(member.getLab() != null ? member.getLab().getAcronym() : null);
        labRepository.findAll().forEach(lab -> {
            boolean changed = false;
            if (member.equals(lab.getDirecteur())) { lab.setDirecteur(null); changed = true; }
            if (member.equals(lab.getDirecteurAdjoint())) { lab.setDirecteurAdjoint(null); changed = true; }
            for (var seat : lab.getComiteGestion()) {
                if (member.equals(seat.getMember())) { seat.setMember(null); changed = true; }
            }
            if (changed) labRepository.save(lab);
        });
        equipeRepository.findByResponsableId(id).forEach(equipe -> {
            equipe.setResponsable(null);
            equipeRepository.save(equipe);
        });
        equipeRepository.findByMembersId(id).forEach(equipe -> {
            equipe.getMembers().remove(member);
            equipeRepository.save(equipe);
        });
        if (member.getLab() != null) {
            member.getLab().getMembers().remove(member);
        }
        memberRepository.delete(member);
    }

    private void applyRelations(Member member, MemberDTO dto) {
        member.setLab(labRepository.findByAcronym(dto.labAcronym()).orElseThrow(() -> new ResourceNotFoundException("Lab", "acronym", dto.labAcronym())));
        // Linking a member to an account changes its authorization scope: only a super admin may change it.
        String currentUserId = member.getUser() != null ? member.getUser().getId() : null;
        String requestedUserId = dto.userId() == null || dto.userId().isBlank() ? null : dto.userId();
        if (!Objects.equals(currentUserId, requestedUserId) && accessControlService.currentScope().isSuperAdmin()) {
            member.setUser(requestedUserId == null ? null : userDao.findById(requestedUserId).orElseThrow(() -> new ResourceNotFoundException("User", "id", requestedUserId)));
        }
    }

    private Member get(String id) { return memberRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Member", "id", id)); }
}
