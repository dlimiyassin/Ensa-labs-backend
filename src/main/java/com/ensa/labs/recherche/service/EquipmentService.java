package com.ensa.labs.recherche.service;

import com.ensa.labs.exception.ResourceNotFoundException;
import com.ensa.labs.recherche.bean.Equipment;
import com.ensa.labs.recherche.dao.EquipmentRepository;
import com.ensa.labs.recherche.dao.LabRepository;
import com.ensa.labs.recherche.dto.EquipmentDTO;
import com.ensa.labs.recherche.mapper.EquipmentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final LabRepository labRepository;
    private final EquipmentMapper equipmentMapper;
    private final AccessControlService accessControlService;

    public EquipmentService(EquipmentRepository equipmentRepository, LabRepository labRepository, EquipmentMapper equipmentMapper, AccessControlService accessControlService) {
        this.equipmentRepository = equipmentRepository;
        this.labRepository = labRepository;
        this.equipmentMapper = equipmentMapper;
        this.accessControlService = accessControlService;
    }

    public List<EquipmentDTO> findAll() { return equipmentRepository.findAll().stream().map(equipmentMapper::toDto).toList(); }

    public EquipmentDTO findById(String id) { return equipmentMapper.toDto(get(id)); }

    public List<EquipmentDTO> findByLabAcronym(String acronym) { return equipmentRepository.findByLabAcronym(acronym).stream().map(equipmentMapper::toDto).toList(); }

    @Transactional
    public EquipmentDTO create(EquipmentDTO dto) {
        accessControlService.assertCanManageLab(dto.labAcronym());
        Equipment equipment = new Equipment();
        equipmentMapper.copyToEntity(dto, equipment);
        equipment.setLab(labRepository.findByAcronym(dto.labAcronym()).orElseThrow(() -> new ResourceNotFoundException("Lab", "acronym", dto.labAcronym())));
        return equipmentMapper.toDto(equipmentRepository.save(equipment));
    }

    @Transactional
    public EquipmentDTO update(String id, EquipmentDTO dto) {
        Equipment equipment = get(id);
        accessControlService.assertCanManageLab(equipment.getLab().getAcronym());
        accessControlService.assertCanManageLab(dto.labAcronym());
        equipmentMapper.copyToEntity(dto, equipment);
        equipment.setLab(labRepository.findByAcronym(dto.labAcronym()).orElseThrow(() -> new ResourceNotFoundException("Lab", "acronym", dto.labAcronym())));
        return equipmentMapper.toDto(equipmentRepository.save(equipment));
    }

    @Transactional
    public void delete(String id) {
        Equipment equipment = get(id);
        accessControlService.assertCanManageLab(equipment.getLab().getAcronym());
        equipmentRepository.delete(equipment);
    }

    private Equipment get(String id) { return equipmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Equipment", "id", id)); }
}
