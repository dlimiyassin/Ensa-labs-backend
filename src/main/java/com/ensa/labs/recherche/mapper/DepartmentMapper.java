package com.ensa.labs.recherche.mapper;

import com.ensa.labs.recherche.bean.Department;
import com.ensa.labs.recherche.dto.DepartmentDTO;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {
    public DepartmentDTO toDto(Department department) {
        return new DepartmentDTO(department.getId(), department.getName());
    }

    public Department toEntity(DepartmentDTO dto) {
        Department department = new Department();
        department.setId(dto.id());
        department.setName(dto.name());
        return department;
    }
}
