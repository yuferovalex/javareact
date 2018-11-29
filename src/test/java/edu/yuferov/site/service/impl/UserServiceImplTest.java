package edu.yuferov.site.service.impl;

import edu.yuferov.site.model.Role;
import edu.yuferov.site.model.User;
import edu.yuferov.site.repository.UserRepository;
import edu.yuferov.site.util.Strings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserServiceImpl service;

    private Role role;
    private User user;

    @Before
    public void setup() {
        role = new Role("title", "description");
        role.setId(1L);
        user = new User("Alex Yuferov", role);
        user.setId(1L);
    }

    @Test
    public void shouldSaveUser() {
        service.save(user);

        verify(repository).save(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveShouldCheckUserNotNull() {
        service.save(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveShouldCheckUserNameIsNotBlank() {
        user.setName("  ");

        service.save(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveShouldCheckUserNameIsNotNull() {
        user.setName(null);

        service.save(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveShouldCheckUserNameIsLessThan30() {
        String s = Strings.repeated("s", 31);
        user.setName(s);

        service.save(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveShouldCheckUserNameHaveOnlyLetters1() {
        user.setName("Alex123");

        service.save(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveShouldCheckUserNameHaveOnlyLetters2() {
        user.setName("Yuferov, A.");

        service.save(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveShouldCheckUserNameHaveOnlyLetters3() {
        user.setName("Yuferov/Alex");

        service.save(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveShouldCheckUserNameHaveOnlyLetters4() {
        user.setName("Alex' and 1 = 1");

        service.save(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveShouldCheckUserNameHaveOnlyLetters5() {
        user.setName("Alex_Yuferov");

        service.save(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveShouldCheckUserNameHaveOnlyLetters6() {
        user.setName("Alex - Yuferov");

        service.save(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveShouldCheckUserNameHaveOnlyLetters7() {
        user.setName("Alex + Yuferov");

        service.save(user);
    }

    @Test
    public void saveShouldAcceptNamesWithHyphen() {
        user.setName("Andrey Demidov-Andriyanov");

        service.save(user);
    }

    @Test
    public void saveShouldAcceptCyrillicNames() {
        user.setName("Александр Юферов");

        service.save(user);
    }

    @Test
    public void shouldDeleteUser() {
        service.delete(user.getId());

        verify(repository).deleteById(user.getId());
    }

    @Test
    public void shouldGetAll() {
        service.getAll();

        verify(repository).findAll();
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveShouldCheckRoleNotNull() {
        user.setRole(null);

        service.save(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveShouldCheckRoleIdNotNull() {
        user.getRole().setId(null);

        service.save(user);
    }

    @Test
    public void shouldFindUserById() {
        service.findById(1L);

        verify(repository).findById(1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findByIdShouldCheckIdNotNull() {
        service.findById(null);
    }
}