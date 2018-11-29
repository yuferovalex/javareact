package edu.yuferov.site.controller;

import edu.yuferov.site.model.Role;
import edu.yuferov.site.service.RoleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleControllerTest {

    @Mock
    private RoleService service;

    @InjectMocks
    private RoleController controller;

    private Role role;

    @Before
    public void setup() {
        role = new Role();
        role.setId(1L);
        role.setTitle("title");
        role.setDescription("description");
    }

    @Test
    public void shouldCreateRoles() {
        role.setId(null);
        doAnswer(invocation -> {
            role.setId(5L);
            return null;
        }).when(service).save(role);

        ResponseEntity<RoleController.CreateResponse> response = controller.create(role);

        verify(service).save(role);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        RoleController.CreateResponse responseBody = response.getBody();
        assertThat(responseBody, is(notNullValue()));
        assertThat(responseBody.success, is(true));
        assertThat(responseBody.error, is(nullValue()));
        assertThat(responseBody.id, is(5L));
    }

    @Test
    public void shouldUpdateRoles() {
        ResponseEntity<RoleController.Response> response = controller.update(role.getId(), role);

        verify(service).save(role);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        RoleController.Response responseBody = response.getBody();
        assertThat(responseBody, is(notNullValue()));
        assertThat(responseBody.success, is(true));
        assertThat(responseBody.error, is(nullValue()));
    }

    @Test
    public void shouldDeleteRoles() {
        ResponseEntity<RoleController.Response> response = controller.delete(role.getId());

        verify(service).delete(role.getId());
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        RoleController.Response responseBody = response.getBody();
        assertThat(responseBody, is(notNullValue()));
        assertThat(responseBody.success, is(true));
        assertThat(responseBody.error, is(nullValue()));
    }

    @Test
    public void createShouldCatchIllegalArgumentException() {
        final String message = "message";
        role.setId(null);
        doThrow(new IllegalArgumentException(message)).when(service).save(role);

        ResponseEntity<RoleController.CreateResponse> response = controller.create(role);

        verify(service).save(role);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        RoleController.CreateResponse responseBody = response.getBody();
        assertThat(responseBody, is(notNullValue()));
        assertThat(responseBody.success, is(false));
        assertThat(responseBody.error, is(message));
        assertThat(responseBody.id, is(nullValue()));
    }

    @Test
    public void updateShouldCatchIllegalArgumentException() {
        final String message = "message";
        doThrow(new IllegalArgumentException(message)).when(service).save(role);

        ResponseEntity<RoleController.Response> response = controller.update(role.getId(), role);

        verify(service).save(role);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        RoleController.Response responseBody = response.getBody();
        assertThat(responseBody, is(notNullValue()));
        assertThat(responseBody.success, is(false));
        assertThat(responseBody.error, is(message));
    }

    @Test
    public void deleteShouldCatchIllegalArgumentException() {
        final String message = "message";
        doThrow(new IllegalArgumentException(message)).when(service).delete(role.getId());

        ResponseEntity<RoleController.Response> response = controller.delete(role.getId());

        verify(service).delete(role.getId());
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        RoleController.Response responseBody = response.getBody();
        assertThat(responseBody, is(notNullValue()));
        assertThat(responseBody.success, is(false));
        assertThat(responseBody.error, is(message));
    }

    @Test
    public void shouldGetAll() {
        Iterable<Role> iterable = mock(Iterable.class);
        when(service.getAll()).thenReturn(iterable);

        ResponseEntity<RoleController.GetAllResponse> response = controller.getAll();

        verify(service).getAll();
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        RoleController.GetAllResponse responseBody = response.getBody();
        assertThat(responseBody.success, is(true));
        assertThat(responseBody.error, is(nullValue()));
        assertThat(responseBody.data, is(iterable));
    }

    @Test
    public void shouldFindById() {
        when(service.findById(role.getId())).thenReturn(Optional.ofNullable(role));

        ResponseEntity<RoleController.FindByIdResponse> response = controller.findById(role.getId());

        verify(service).findById(role.getId());
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        RoleController.FindByIdResponse body = response.getBody();
        assertThat(body.role, is(role));
        assertThat(body.success, is(true));
        assertThat(body.error, is(nullValue()));
    }

    @Test
    public void findByIdShouldReturnNullDataIfRoleNotExists() {
        when(service.findById(role.getId())).thenReturn(Optional.empty());

        ResponseEntity<RoleController.FindByIdResponse> response = controller.findById(role.getId());

        verify(service).findById(role.getId());
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        RoleController.FindByIdResponse body = response.getBody();
        assertThat(body.role, is(nullValue()));
        assertThat(body.success, is(true));
        assertThat(body.error, is(nullValue()));
    }
}