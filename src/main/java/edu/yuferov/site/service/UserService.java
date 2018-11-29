package edu.yuferov.site.service;

import edu.yuferov.site.model.User;

import java.util.Optional;

public interface UserService {
    void save(User role);
    void delete(Long id);
    Iterable<User> getAll();
    Optional<User> findById(Long id);
}
