package edu.yuferov.site.service;

import edu.yuferov.site.model.Role;

import java.util.Optional;

public interface RoleService {
    void save(Role role);
    void delete(Long id);
    Optional<Role> findById(Long id);
    Iterable<Role> getAll();
}
