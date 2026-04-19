package com.ensa.labs.research.dto;

import com.ensa.labs.research.bean.enums.ProjectStatus;

import java.util.List;

public record ProjectDTO(
        String id,
        String title,
        ProjectStatus status,
        String labId,
        String teamId,
        List<String> userIds
) {
}
