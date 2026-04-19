package com.ensa.labs.research.mapper;

import com.ensa.labs.research.bean.Department;
import com.ensa.labs.research.dto.DepartmentDTO;
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
