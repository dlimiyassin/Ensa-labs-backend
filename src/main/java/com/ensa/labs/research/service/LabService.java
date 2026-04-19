package com.ensa.labs.research.service;

import com.ensa.labs.exception.ResourceNotFoundException;
import com.ensa.labs.research.bean.Lab;
import com.ensa.labs.research.bean.Member;
import com.ensa.labs.research.dao.*;
import com.ensa.labs.research.dto.LabDTO;
import com.ensa.labs.research.mapper.LabMapper;
import com.ensa.labs.zBase.security.dao.facade.UserDao;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class LabService {
    private final LabRepository labRepository;
    private final DepartmentRepository departmentRepository;
    private final ResearchFieldRepository researchFieldRepository;
    private final TagRepository tagRepository;
    private final UserDao userDao;
    private final MemberRepository memberRepository;
    private final LabMapper labMapper;

    public LabService(LabRepository labRepository, DepartmentRepository departmentRepository, ResearchFieldRepository researchFieldRepository, TagRepository tagRepository, UserDao userDao, MemberRepository memberRepository, LabMapper labMapper) {
        this.labRepository = labRepository;
        this.departmentRepository = departmentRepository;
        this.researchFieldRepository = researchFieldRepository;
        this.tagRepository = tagRepository;
        this.userDao = userDao;
        this.memberRepository = memberRepository;
        this.labMapper = labMapper;
    }

    public List<LabDTO> findAll() { return labRepository.findAll().stream().map(labMapper::toDto).toList(); }
    public LabDTO findById(String id) { return labMapper.toDto(get(id)); }

    public LabDTO create(LabDTO dto) {
        Lab lab = labMapper.toEntity(dto);
        applyRelations(lab, dto);
        return labMapper.toDto(labRepository.save(lab));
    }

    public LabDTO update(String id, LabDTO dto) {
        Lab lab = get(id);
        lab.setTitleFr(dto.titleFr());
        lab.setTitleEn(dto.titleEn());
        lab.setAcronym(dto.acronym());
        lab.setUniversity(dto.university());
        lab.setProgram(dto.program());
        lab.setAccreditationStart(dto.accreditationStart());
        lab.setAccreditationEnd(dto.accreditationEnd());
        lab.setEstablishment(dto.establishment());
        lab.setPhone(dto.phone());
        lab.setEmail(dto.email());
        applyRelations(lab, dto);
        return labMapper.toDto(labRepository.save(lab));
    }

    public void delete(String id) { labRepository.delete(get(id)); }

    private void applyRelations(Lab lab, LabDTO dto) {
        if (dto.departmentId() != null) {
            lab.setDepartment(departmentRepository.findById(dto.departmentId()).orElseThrow(() -> new ResourceNotFoundException("Department", "id", dto.departmentId())));
        }
        if (dto.researchFieldIds() != null) {
            lab.setResearchFields(new HashSet<>(dto.researchFieldIds().stream().map(id -> researchFieldRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ResearchField", "id", id))).toList()));
        }
        if (dto.userIds() != null) {
            lab.setUsers(new HashSet<>(dto.userIds().stream().map(id -> userDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id))).toList()));
        }
        if (dto.tagIds() != null) {
            lab.setTags(new HashSet<>(dto.tagIds().stream().map(id -> tagRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tag", "id", id))).toList()));
        }
        lab.setDirector(resolveMember(dto.directorId()));
        lab.setDeputyDirector(resolveMember(dto.deputyDirectorId()));
    }

    private Member resolveMember(String memberId) {
        return memberId == null ? null : memberRepository.findById(memberId).orElseThrow(() -> new ResourceNotFoundException("Member", "id", memberId));
    }

    private Lab get(String id) { return labRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lab", "id", id)); }
}
