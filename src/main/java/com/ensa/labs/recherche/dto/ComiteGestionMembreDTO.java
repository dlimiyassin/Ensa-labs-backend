package com.ensa.labs.recherche.dto;

/**
 * @param memberId lab member holding this committee seat (optional)
 * @param image    that member's picture, read-only
 */
public record ComiteGestionMembreDTO(
        String nomEnseignant,
        String roleComite,
        String memberId,
        String image
) {
}
