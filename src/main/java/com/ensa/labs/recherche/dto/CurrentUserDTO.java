package com.ensa.labs.recherche.dto;

import java.util.List;

/**
 * The authenticated account as seen by the dashboard: authorization roles plus the
 * lab / team scope derived from the linked member.
 */
public record CurrentUserDTO(
        String id,
        String username,
        String firstName,
        String lastName,
        String email,
        List<String> roles,
        String memberId,
        String labAcronym,
        List<String> ledEquipeIds,
        String image
) {
}
