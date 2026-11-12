package com.ensa.labs.recherche.service;

import com.ensa.labs.exception.GlobalException;
import com.ensa.labs.exception.ResourceNotFoundException;
import com.ensa.labs.recherche.bean.AxeRecherche;
import com.ensa.labs.recherche.bean.Equipe;
import com.ensa.labs.recherche.bean.Member;
import com.ensa.labs.recherche.dao.AxeRechercheRepository;
import com.ensa.labs.recherche.dao.EquipeRepository;
import com.ensa.labs.recherche.dao.LabRepository;
import com.ensa.labs.recherche.dao.MemberRepository;
import com.ensa.labs.recherche.dao.PublicationRepository;
import com.ensa.labs.recherche.dto.EquipeDTO;
import com.ensa.labs.recherche.mapper.EquipeMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
public class EquipeService {
    private final EquipeRepository equipeRepository;
    private final LabRepository labRepository;
    private final AxeRechercheRepository axeRechercheRepository;
    private final MemberRepository memberRepository;
    private final PublicationRepository publicationRepository;
    private final EquipeMapper equipeMapper;
    private final AccessControlService accessControlService;

    public EquipeService(EquipeRepository equipeRepository, LabRepository labRepository, AxeRechercheRepository axeRechercheRepository, MemberRepository memberRepository, PublicationRepository publicationRepository, EquipeMapper equipeMapper, AccessControlService accessControlService) {
        this.equipeRepository = equipeRepository;
        this.labRepository = labRepository;
        this.axeRechercheRepository = axeRechercheRepository;
        this.memberRepository = memberRepository;
        this.publicationRepository = publicationRepository;
        this.equipeMapper = equipeMapper;
        this.accessControlService = accessControlService;
    }

    public List<EquipeDTO> findAll() { return equipeRepository.findAll().stream().map(equipeMapper::toDto).toList(); }

    public EquipeDTO findById(String id) { return equipeMapper.toDto(get(id)); }

    public List<EquipeDTO> findByLabAcronym(String acronym) { return equipeRepository.findByLabAcronym(acronym).stream().map(equipeMapper::toDto).toList(); }

    @Transactional
    public EquipeDTO create(EquipeDTO dto) {
        accessControlService.assertCanManageLab(dto.labAcronym());
        Equipe equipe = equipeMapper.toEntity(dto);
        equipe.setId(null);
        Equipe saved = equipeRepository.save(applyRelations(equipe, dto));
        return equipeMapper.toDto(saved);
    }

    @Transactional
    public EquipeDTO update(String id, EquipeDTO dto) {
        Equipe equipe = get(id);
        accessControlService.assertCanManageLab(equipe.getLab().getAcronym());
        accessControlService.assertCanManageLab(dto.labAcronym());
        equipe.setName(dto.name());
        return equipeMapper.toDto(equipeRepository.save(applyRelations(equipe, dto)));
    }

    /** Deletes the team and its research axes; its publications stay in the lab without team. */
    @Transactional
    public void delete(String id) {
        Equipe equipe = get(id);
        accessControlService.assertCanManageLab(equipe.getLab().getAcronym());
        publicationRepository.findByEquipeId(id).forEach(publication -> {
            publication.setEquipe(null);
            publicationRepository.save(publication);
        });
        axeRechercheRepository.deleteAll(axeRechercheRepository.findByEquipeId(id));
        equipe.getAxesRecherche().clear();
        equipe.getMembers().clear();
        equipe.getLab().getEquipes().remove(equipe);
        equipeRepository.delete(equipe);
    }

    private Equipe applyRelations(Equipe equipe, EquipeDTO dto) {
        equipe.setLab(labRepository.findByAcronym(dto.labAcronym()).orElseThrow(() -> new ResourceNotFoundException("Lab", "acronym", dto.labAcronym())));
        if (dto.axesRecherche() != null) {
            var axes = new HashSet<AxeRecherche>(dto.axesRecherche().stream()
                    .filter(a -> a.id() != null)
                    .map(a -> axeRechercheRepository.findById(a.id()).orElseThrow(() -> new ResourceNotFoundException("AxeRecherche", "id", a.id())))
                    .toList());
            axes.forEach(a -> {
                a.setEquipe(equipe);
                a.setLab(null);
            });
            equipe.setAxesRecherche(axes);
        }
        Member responsable = dto.responsable() == null || dto.responsable().id() == null ? null
                : memberRepository.findById(dto.responsable().id()).orElseThrow(() -> new ResourceNotFoundException("Member", "id", dto.responsable().id()));
        equipe.setResponsable(responsable);
        var members = dto.members() == null ? new HashSet<Member>() : new HashSet<>(dto.members().stream()
                .filter(m -> m.id() != null)
                .map(m -> memberRepository.findById(m.id()).orElseThrow(() -> new ResourceNotFoundException("Member", "id", m.id())))
                .toList());
        if (responsable != null) {
            members.add(responsable);
        }
        boolean outsider = members.stream().anyMatch(m -> m.getLab() == null || !m.getLab().getId().equals(equipe.getLab().getId()));
        if (outsider) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "Team members must belong to the team's lab");
        }
        equipe.setMembers(members);
        return equipe;
    }

    private Equipe get(String id) { return equipeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Equipe", "id", id)); }
}
