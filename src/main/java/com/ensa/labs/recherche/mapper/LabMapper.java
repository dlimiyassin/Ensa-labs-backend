package com.ensa.labs.recherche.mapper;

import com.ensa.labs.recherche.bean.Lab;
import com.ensa.labs.recherche.dto.LabDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class LabMapper {
    private final MemberMapper memberMapper;
    private final ComiteGestionMapper comiteGestionMapper;
    private final DomaineRechercheMapper domaineRechercheMapper;
    private final AxeRechercheMapper axeRechercheMapper;
    private final EquipmentMapper equipmentMapper;
    private final CompetenceMapper competenceMapper;
    private final ProductionMapper productionMapper;
    private final DepartmentMapper departmentMapper;
    private final EquipeMapper equipeMapper;
    private final TagMapper tagMapper;

    public LabMapper(MemberMapper memberMapper,
                     ComiteGestionMapper comiteGestionMapper,
                     DomaineRechercheMapper domaineRechercheMapper,
                     AxeRechercheMapper axeRechercheMapper,
                     EquipmentMapper equipmentMapper,
                     CompetenceMapper competenceMapper,
                     ProductionMapper productionMapper,
                     DepartmentMapper departmentMapper,
                     EquipeMapper equipeMapper,
                     TagMapper tagMapper) {
        this.memberMapper = memberMapper;
        this.comiteGestionMapper = comiteGestionMapper;
        this.domaineRechercheMapper = domaineRechercheMapper;
        this.axeRechercheMapper = axeRechercheMapper;
        this.equipmentMapper = equipmentMapper;
        this.competenceMapper = competenceMapper;
        this.productionMapper = productionMapper;
        this.departmentMapper = departmentMapper;
        this.equipeMapper = equipeMapper;
        this.tagMapper = tagMapper;
    }

    public LabDTO toDto(Lab lab) {
        return new LabDTO(
                lab.getId(),
                lab.getTitleFr(),
                lab.getTitleEn(),
                lab.getAcronym(),
                lab.getUniversity(),
                lab.getProgram(),
                lab.getAccreditationStart(),
                lab.getAccreditationEnd(),
                lab.getEstablishment(),
                lab.getPhone(),
                lab.getEmail(),
                lab.getDirecteur() != null ? memberMapper.toDto(lab.getDirecteur()) : null,
                lab.getDirecteurAdjoint() != null ? memberMapper.toDto(lab.getDirecteurAdjoint()) : null,
                lab.getMembers().stream().map(memberMapper::toDto).toList(),
                lab.getComiteGestion().stream().map(comiteGestionMapper::toDto).toList(),
                lab.getDomainesRecherche().stream().map(domaineRechercheMapper::toDto).toList(),
                lab.getAxesRecherche().stream().map(axeRechercheMapper::toDto).toList(),
                lab.getEquipments().stream().map(equipmentMapper::toDto).toList(),
                lab.getCompetences().stream().map(competenceMapper::toDto).toList(),
                lab.getProduction() != null ? productionMapper.toDto(lab.getProduction()) : null,
                lab.getDepartment() != null ? departmentMapper.toDto(lab.getDepartment()) : null,
                lab.getEquipes().stream().map(equipeMapper::toDto).toList(),
                lab.getTags().stream().map(tagMapper::toDto).toList()
        );
    }

    public Lab toEntity(LabDTO dto) {
        Lab lab = new Lab();
        lab.setId(dto.id());
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
        lab.setComiteGestion(new ArrayList<>());
        return lab;
    }
}
