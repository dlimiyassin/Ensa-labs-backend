package com.ensa.labs.recherche.service;

import com.ensa.labs.exception.ResourceAlreadyExistsException;
import com.ensa.labs.exception.ResourceNotFoundException;
import com.ensa.labs.recherche.bean.DomaineRecherche;
import com.ensa.labs.recherche.dao.DomaineRechercheRepository;
import com.ensa.labs.recherche.dto.ThematiqueRechercheDTO;
import com.ensa.labs.recherche.mapper.ThematiqueRechercheMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

/** Global catalogue of research themes; labs link to them (SUPER_ADMIN manages the catalogue). */
@Service
public class ThematiqueRechercheService {
    private final DomaineRechercheRepository repository;
    private final ThematiqueRechercheMapper mapper;

    public ThematiqueRechercheService(DomaineRechercheRepository repository, ThematiqueRechercheMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<ThematiqueRechercheDTO> findAll() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(DomaineRecherche::getName, String.CASE_INSENSITIVE_ORDER))
                .map(mapper::toDto)
                .toList();
    }

    public ThematiqueRechercheDTO findById(String id) { return mapper.toDto(get(id)); }

    @Transactional
    public ThematiqueRechercheDTO create(ThematiqueRechercheDTO dto) {
        String name = dto.name().trim();
        repository.findByName(name).ifPresent(existing -> {
            throw new ResourceAlreadyExistsException("Thematique", "name", name);
        });
        DomaineRecherche entity = new DomaineRecherche();
        entity.setName(name);
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public ThematiqueRechercheDTO update(String id, ThematiqueRechercheDTO dto) {
        DomaineRecherche entity = get(id);
        String name = dto.name().trim();
        repository.findByName(name).filter(other -> !other.getId().equals(id)).ifPresent(existing -> {
            throw new ResourceAlreadyExistsException("Thematique", "name", name);
        });
        entity.setName(name);
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public void delete(String id) {
        DomaineRecherche entity = get(id);
        entity.getLabs().forEach(lab -> lab.getDomainesRecherche().remove(entity));
        entity.getLabs().clear();
        repository.delete(entity);
    }

    /** Resolves a theme by id, or by name (creating it when missing). */
    @Transactional
    public DomaineRecherche resolve(ThematiqueRechercheDTO dto) {
        if (dto.id() != null && !dto.id().isBlank()) {
            return get(dto.id());
        }
        String name = dto.name() == null ? "" : dto.name().trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("A thematique needs an id or a name");
        }
        return repository.findByName(name).orElseGet(() -> {
            DomaineRecherche entity = new DomaineRecherche();
            entity.setName(name);
            return repository.save(entity);
        });
    }

    private DomaineRecherche get(String id) { return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Thematique", "id", id)); }
}
