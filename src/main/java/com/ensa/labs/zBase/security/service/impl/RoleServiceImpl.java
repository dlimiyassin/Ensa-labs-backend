package com.ensa.labs.zBase.security.service.impl;

import com.ensa.labs.exception.ResourceAlreadyExistsException;
import com.ensa.labs.exception.ResourceNotFoundException;
import com.ensa.labs.zBase.security.bean.Role;
import com.ensa.labs.zBase.security.dao.criteria.RoleCriteria;
import com.ensa.labs.zBase.security.dao.facade.RoleDao;
import com.ensa.labs.zBase.security.service.facade.RoleService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public Role createRole(String roleName) {
        existingRole(roleName);
        return roleDao.save(new Role(roleName));
    }

    @Override
    public Role findByName(String name) {
        return roleDao.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", name));
    }

    @Override
    public Role save(Role role) {
        existingRole(role.getName());
        return roleDao.save(role);
    }

    @Override
    public Role update(Role role) {
        findById(role.getId());
        return roleDao.save(role);
    }

    @Override
    public void delete(String id) {
        findById(id);
        roleDao.deleteById(id);
    }

    @Override
    public Role findById(String id) {
        return roleDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));
    }

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    @Override
    public void existingRole(String roleName) {
        roleDao.findByName(roleName).ifPresent(role -> {
            throw new ResourceAlreadyExistsException("Role", "name", roleName);
        });
    }

    @Override
    public List<Role> findByCriteria(RoleCriteria criteria) {
        if (criteria == null) return findAll();

        Specification<Role> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.id() != null) {
                predicates.add(cb.equal(root.get("id"), criteria.id()));
            }

            if (criteria.name() != null) {
                predicates.add(cb.like(
                        cb.lower(root.get("name")),
                        "%" + criteria.name().toLowerCase() + "%"
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return roleDao.findAll(spec);
    }
}