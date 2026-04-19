package com.ensa.labs.research.service;

import com.ensa.labs.exception.ResourceNotFoundException;
import com.ensa.labs.research.bean.Publication;
import com.ensa.labs.research.dao.LabRepository;
import com.ensa.labs.research.dao.PublicationRepository;
import com.ensa.labs.research.dao.TeamRepository;
import com.ensa.labs.research.dto.PublicationDTO;
import com.ensa.labs.research.mapper.PublicationMapper;
import com.ensa.labs.zBase.security.dao.facade.UserDao;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class PublicationService {
    private final PublicationRepository publicationRepository;
    private final UserDao userDao;
    private final LabRepository labRepository;
    private final TeamRepository teamRepository;
    private final PublicationMapper publicationMapper;

    public PublicationService(PublicationRepository publicationRepository, UserDao userDao, LabRepository labRepository, TeamRepository teamRepository, PublicationMapper publicationMapper) {
        this.publicationRepository = publicationRepository;
        this.userDao = userDao;
        this.labRepository = labRepository;
        this.teamRepository = teamRepository;
        this.publicationMapper = publicationMapper;
    }

    public List<PublicationDTO> findAll() { return publicationRepository.findAll().stream().map(publicationMapper::toDto).toList(); }
    public PublicationDTO findById(String id) { return publicationMapper.toDto(get(id)); }

    public PublicationDTO create(PublicationDTO dto) {
        Publication publication = publicationMapper.toEntity(dto);
        applyRelations(publication, dto);
        return publicationMapper.toDto(publicationRepository.save(publication));
    }

    public PublicationDTO update(String id, PublicationDTO dto) {
        Publication publication = get(id);
        publication.setTitle(dto.title());
        publication.setType(dto.type());
        publication.setPublicationYear(dto.publicationYear());
        applyRelations(publication, dto);
        return publicationMapper.toDto(publicationRepository.save(publication));
    }

    public void delete(String id) { publicationRepository.delete(get(id)); }

    private void applyRelations(Publication publication, PublicationDTO dto) {
        publication.setAuthors(new HashSet<>(dto.authorIds().stream().map(id -> userDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id))).toList()));
        publication.setLab(dto.labId() == null ? null : labRepository.findById(dto.labId()).orElseThrow(() -> new ResourceNotFoundException("Lab", "id", dto.labId())));
        publication.setTeam(dto.teamId() == null ? null : teamRepository.findById(dto.teamId()).orElseThrow(() -> new ResourceNotFoundException("Team", "id", dto.teamId())));
    }

    private Publication get(String id) { return publicationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Publication", "id", id)); }
}
