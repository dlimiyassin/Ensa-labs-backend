package com.ensa.labs.zBase.security.dao.facade;

import com.ensa.labs.zBase.security.bean.Role;
import com.ensa.labs.zBase.security.bean.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UtilisateurDao extends JpaRepository<Utilisateur, String> {

    Optional<Utilisateur> findByNomUtilisateur(String nomUtilisateur);

    Optional<Utilisateur> findByCourrierElectronique(String courrierElectronique);

    List<Utilisateur> findUtilisateursByRoles(Set<Role> roles);
}
