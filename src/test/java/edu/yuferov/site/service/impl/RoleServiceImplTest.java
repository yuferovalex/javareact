package edu.yuferov.site.service.impl;

import edu.yuferov.site.model.Role;
import edu.yuferov.site.repository.RoleRepository;
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
public class RoleServiceImplTest {

    @Mock
    private RoleRepository repository;

    @InjectMocks
    private RoleServiceImpl service;

    private Role role;

    @Test
    public void shouldSaveRoles() {
        service.save(role);

        verify(repository).save(role);
    }

    @Before
    public void setup() {
        role = new Role("title", "description");
        role.setId(1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveShouldCheckRoleNotNull() {
        service.save(null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void saveShouldCheckTitleNotEmpty() {
        role.setTitle("");
        service.save(role);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveShouldCheckTitleNotEmpty2() {
        role.setTitle("    ");
        service.save(role);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveShouldCheckTitleNotNull() {
        role.setTitle(null);
        service.save(role);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveShouldCheckDescriptionNotEmpty() {
        role.setDescription("");
        service.save(role);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveShouldCheckDescriptionNotEmpty2() {
        role.setDescription("    ");
        service.save(role);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldCheckDescriptionNotNull() {
        role.setDescription(null);
        service.save(role);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveShouldCheckTitleLengthBeLessThan10() {
        role.setTitle(Strings.repeated("s", 11));
        service.save(role);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveShouldCheckDescriptionLengthBeLessThan30() {
        role.setTitle(Strings.repeated("s", 31));
        service.save(role);
    }

    @Test
    public void shouldDeleteRoles() {
        service.delete(role.getId());

        verify(repository).deleteById(role.getId());
    }

    @Test
    public void shouldGetAllRoles() {
        service.getAll();

        verify(repository).findAll();
    }

    @Test
    public void shouldFindRoleById() {
        service.findById(role.getId());

        verify(repository).findById(role.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void findByIdShouldCheckIdNotNull() {
        service.findById(null);
    }

}