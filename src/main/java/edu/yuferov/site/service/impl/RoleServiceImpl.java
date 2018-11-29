package edu.yuferov.site.service.impl;

import edu.yuferov.site.model.Role;
import edu.yuferov.site.repository.RoleRepository;
import edu.yuferov.site.service.RoleService;
import edu.yuferov.site.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository repository;

    @Override
    public void save(Role role) {
        Assert.requireNonNull(role, "Role can not be null");
        Assert.requireNonBlank(role.getTitle(), "Title can not be blank");
        Assert.requiredLength(role.getTitle(), 1, 10, "Title length must be from 1 to 10 symbols");
        Assert.requireNonBlank(role.getDescription(), "Description can not be blank");
        Assert.requiredLength(role.getDescription(), 1, 30, "Description length must be from 1 to 30 symbols");
        repository.save(role);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Iterable<Role> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Role> findById(Long id) {
        Assert.requireNonNull(id, "Id can not be null");
        return repository.findById(id);
    }

}
