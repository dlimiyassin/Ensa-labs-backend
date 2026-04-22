package com.ensa.labs.recherche.mapper;

import com.ensa.labs.recherche.bean.Thesis;
import com.ensa.labs.recherche.dto.ThesisDTO;
import org.springframework.stereotype.Component;

@Component
public class ThesisMapper {
    public ThesisDTO toDto(Thesis thesis) {
        return new ThesisDTO(thesis.getId(), thesis.getAuthor(), thesis.getTitle(), thesis.getDefenseDate(), thesis.getSupervisor(),
                thesis.getLab() != null ? thesis.getLab().getAcronym() : null);
    }
}
