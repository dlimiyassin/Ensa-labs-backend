package com.ensa.labs.recherche.service;

import com.ensa.labs.exception.GlobalException;
import com.ensa.labs.exception.ResourceAlreadyExistsException;
import com.ensa.labs.exception.ResourceNotFoundException;
import com.ensa.labs.recherche.bean.Member;
import com.ensa.labs.recherche.dao.MemberRepository;
import com.ensa.labs.recherche.dao.PublicationRepository;
import com.ensa.labs.recherche.dto.UserDTO;
import com.ensa.labs.recherche.mapper.UserMapper;
import com.ensa.labs.zBase.security.bean.User;
import com.ensa.labs.zBase.security.dao.facade.RoleDao;
import com.ensa.labs.zBase.security.dao.facade.UserDao;
import com.ensa.labs.zBase.testing.TestProfileAccess;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/** Account administration (SUPER_ADMIN only, enforced by the security configuration). */
@Service
public class UserManagementService {
    private static final String FALLBACK_PASSWORD = "ChangeMe123!";

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final MemberRepository memberRepository;
    private final PublicationRepository publicationRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AccessControlService accessControlService;

    public UserManagementService(UserDao userDao, RoleDao roleDao, MemberRepository memberRepository, PublicationRepository publicationRepository,
                                 PasswordEncoder passwordEncoder, UserMapper userMapper, AccessControlService accessControlService) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.memberRepository = memberRepository;
        this.publicationRepository = publicationRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.accessControlService = accessControlService;
    }

    public List<UserDTO> findAll() {
        boolean testSession = isTestSession();
        return userDao.findAll().stream()
                .filter(account -> !testSession || TestProfileAccess.isTestAccount(account.getUsername()))
                .map(userMapper::toDto)
                .toList();
    }

    public UserDTO findById(String id) {
        User account = get(id);
        if (isTestSession() && !TestProfileAccess.isTestAccount(account.getUsername())) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        return userMapper.toDto(account);
    }

    private boolean isTestSession() {
        return accessControlService.currentUser()
                .map(account -> TestProfileAccess.isTestAccount(account.getUsername())).orElse(false);
    }

    @Transactional
    public UserDTO create(UserDTO dto) {
        assertUnique(dto, null);
        User user = userMapper.toEntity(dto);
        user.setId(null);
        String initialPassword = hasText(dto.password()) ? dto.password()
                : hasText(user.getCin()) ? user.getCin() : FALLBACK_PASSWORD;
        user.setPassword(passwordEncoder.encode(initialPassword));
        applyRoles(user, dto);
        User saved = userDao.save(user);
        linkMember(saved, dto.memberId());
        return userMapper.toDto(saved);
    }

    @Transactional
    public UserDTO update(String id, UserDTO dto) {
        User user = get(id);
        assertUnique(dto, id);
        user.setUsername(dto.username());
        user.setCin(dto.cin());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(hasText(dto.email()) ? dto.email() : null);
        user.setPhoneNumber(dto.phoneNumber());
        user.setEnabled(dto.enabled());
        if (dto.status() != null) user.setStatus(dto.status());
        if (hasText(dto.password())) user.setPassword(passwordEncoder.encode(dto.password()));
        applyRoles(user, dto);
        User saved = userDao.save(user);
        linkMember(saved, dto.memberId());
        return userMapper.toDto(saved);
    }

    @Transactional
    public void delete(String id) {
        User user = get(id);
        accessControlService.currentUser()
                .filter(current -> current.getId().equals(id))
                .ifPresent(current -> {
                    throw new GlobalException(HttpStatus.BAD_REQUEST, "You cannot delete your own account");
                });
        memberRepository.findByUserId(id).ifPresent(member -> {
            member.setUser(null);
            memberRepository.save(member);
        });
        publicationRepository.findAll().stream()
                .filter(p -> p.getCreatedBy() != null && id.equals(p.getCreatedBy().getId()))
                .forEach(p -> {
                    p.setCreatedBy(null);
                    publicationRepository.save(p);
                });
        userDao.delete(user);
    }

    private void applyRoles(User user, UserDTO dto) {
        user.setRoles(new HashSet<>(dto.roleIds().stream().map(roleId -> roleDao.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId))).toList()));
    }

    /** One account per member: moves the link from any previous member to the requested one. */
    private void linkMember(User user, String memberId) {
        Member previous = memberRepository.findByUserId(user.getId()).orElse(null);
        String previousId = previous != null ? previous.getId() : null;
        String requestedId = hasText(memberId) ? memberId : null;
        if (Objects.equals(previousId, requestedId)) return;
        if (previous != null) {
            previous.setUser(null);
            memberRepository.saveAndFlush(previous);
        }
        if (requestedId != null) {
            Member member = memberRepository.findById(requestedId).orElseThrow(() -> new ResourceNotFoundException("Member", "id", requestedId));
            if (member.getUser() != null && !member.getUser().getId().equals(user.getId())) {
                throw new GlobalException(HttpStatus.CONFLICT, "This member is already linked to account " + member.getUser().getUsername());
            }
            member.setUser(user);
            memberRepository.save(member);
        }
    }

    private void assertUnique(UserDTO dto, String currentId) {
        if (TestProfileAccess.isTestAccount(dto.username())) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "This username is reserved for test profiles");
        }
        userDao.findByUsername(dto.username())
                .filter(other -> !other.getId().equals(currentId))
                .ifPresent(other -> {
                    throw new ResourceAlreadyExistsException("User", "username", dto.username());
                });
        if (hasText(dto.email())) {
            userDao.findByEmail(dto.email())
                    .filter(other -> !other.getId().equals(currentId))
                    .ifPresent(other -> {
                        throw new ResourceAlreadyExistsException("User", "email", dto.email());
                    });
        }
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private User get(String id) { return userDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id)); }
}
