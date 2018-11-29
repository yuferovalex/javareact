package edu.yuferov.site.service.impl;

import edu.yuferov.site.model.User;
import edu.yuferov.site.repository.UserRepository;
import edu.yuferov.site.service.UserService;
import edu.yuferov.site.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public void save(User user) throws IllegalArgumentException {
        Assert.requireNonNull(user, "User must not be null");
        Assert.requireNonNull(user.getRole(), "Role must not be null");
        Assert.requireNonNull(user.getRole().getId(), "Role id must not be null");
        Assert.requireNonNull(user.getName(), "User name must not be null");
        user.setName(user.getName().trim());
        checkUserName(user.getName());
        repository.save(user);
    }

    @Override
    public void delete(Long id) throws IllegalArgumentException {
        repository.deleteById(id);
    }

    @Override
    public Iterable<User> getAll() {
        return repository.findAll();
    }

    private void checkUserName(String name) {
        final String wordWithHyphenPattern = "\\p{Alpha}+(-\\p{Alpha}+)?";
        final String namePattern = String.format("(?Ui)%s(\\s%s)?", wordWithHyphenPattern, wordWithHyphenPattern);
        Assert.requiredLength(name, 1, 30, "User name must be from 1 to 30 characters length");
        Assert.requireMatchTemplate(name, namePattern, "User name must have only letters and only one space separating name and surname");
    }

    @Override
    public Optional<User> findById(Long id) {
        Assert.requireNonNull(id, "Id must not be null");
        return repository.findById(id);
    }
}