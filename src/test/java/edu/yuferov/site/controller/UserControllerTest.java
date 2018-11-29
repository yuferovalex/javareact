package edu.yuferov.site.controller;

import edu.yuferov.site.model.User;
import edu.yuferov.site.service.UserService;
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
public class UserControllerTest {

    @Mock
    private UserService service;

    @InjectMocks
    private UserController controller;

    @Test
    public void shouldCreate() {
        doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            assertThat(user.getName(), is("Some Name"));
            assertThat(user.getRole(), is(notNullValue()));
            assertThat(user.getRole().getId(), is(1L));
            user.setId(1L);
            return null;
        }).when(service).save(any(User.class));

        UserController.CreateUpdateRequest request = new UserController.CreateUpdateRequest();
        request.name = "Some Name";
        request.roleId = 1L;
        ResponseEntity<UserController.CreateResponse> response = controller.create(request);

        verify(service).save(any());
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        UserController.CreateResponse body = response.getBody();
        assertThat(body.success, is(true));
        assertThat(body.error, is(nullValue()));
        assertThat(body.id, is(1L));
    }

    @Test
    public void createShouldCatchIllegalArgumentException() {
        doThrow(new IllegalArgumentException("message")).when(service).save(any(User.class));

        UserController.CreateUpdateRequest request = new UserController.CreateUpdateRequest();
        request.name = "Some Name";
        request.roleId = 1L;
        ResponseEntity<UserController.CreateResponse> response = controller.create(request);

        verify(service).save(any());
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        UserController.CreateResponse body = response.getBody();
        assertThat(body.success, is(false));
        assertThat(body.error, is("message"));
        assertThat(body.id, is(nullValue()));
    }

    @Test
    public void shouldUpdate() {
        doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            assertThat(user.getName(), is("Some Name"));
            assertThat(user.getRole(), is(notNullValue()));
            assertThat(user.getRole().getId(), is(1L));
            assertThat(user.getId(), is(1L));
            return null;
        }).when(service).save(any(User.class));

        UserController.CreateUpdateRequest request = new UserController.CreateUpdateRequest();
        request.name = "Some Name";
        request.roleId = 1L;
        ResponseEntity<UserController.Response> response = controller.update(1L, request);

        verify(service).save(any());
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        UserController.Response body = response.getBody();
        assertThat(body.success, is(true));
        assertThat(body.error, is(nullValue()));
    }

    @Test
    public void updateShouldCheckIdNotNull() {
        UserController.CreateUpdateRequest request = new UserController.CreateUpdateRequest();
        request.name = "Some Name";
        request.roleId = 1L;
        ResponseEntity<UserController.Response> response = controller.update(null, request);

        verifyZeroInteractions(service);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        UserController.Response body = response.getBody();
        assertThat(body.success, is(false));
        assertThat(body.error, is(notNullValue()));
    }

    @Test
    public void updateShouldCatchIllegalArgumentException() {
        doThrow(new IllegalArgumentException("message")).when(service).save(any(User.class));

        UserController.CreateUpdateRequest request = new UserController.CreateUpdateRequest();
        request.name = "Some Name";
        request.roleId = 1L;
        ResponseEntity<UserController.Response> response = controller.update(1L, request);

        verify(service).save(any());
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        UserController.Response body = response.getBody();
        assertThat(body.success, is(false));
        assertThat(body.error, is("message"));
    }

    @Test
    public void shouldDelete() {
        ResponseEntity<ControllerBase.Response> response = controller.delete(1L);

        verify(service).delete(1L);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        UserController.Response body = response.getBody();
        assertThat(body.success, is(true));
        assertThat(body.error, is(nullValue()));
    }

    @Test
    public void deleteShouldCatchIllegalArgumentException() {
        doThrow(new IllegalArgumentException("message")).when(service).delete(1L);

        ResponseEntity<ControllerBase.Response> response = controller.delete(1L);

        verify(service).delete(1L);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        UserController.Response body = response.getBody();
        assertThat(body.success, is(false));
        assertThat(body.error, is("message"));
    }

    @Test
    public void shouldGetAll() {
        Iterable<User> iterable = mock(Iterable.class);
        when(service.getAll()).thenReturn(iterable);

        ResponseEntity<UserController.GetAllResponse> response = controller.getAll();

        verify(service).getAll();
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        UserController.GetAllResponse body = response.getBody();
        assertThat(body.success, is(true));
        assertThat(body.error, is(nullValue()));
        assertThat(body.data, is(iterable));
    }

    @Test
    public void getAllShouldCatchIllegalArgumentException() {
        doThrow(new IllegalArgumentException("message")).when(service).getAll();

        ResponseEntity<UserController.GetAllResponse> response = controller.getAll();

        verify(service).getAll();
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        UserController.GetAllResponse body = response.getBody();
        assertThat(body.success, is(false));
        assertThat(body.error, is("message"));
        assertThat(body.data, is(nullValue()));
    }

    @Test
    public void shouldFindById() {
        User user = new User();
        when(service.findById(1L)).thenReturn(Optional.ofNullable(user));

        ResponseEntity<UserController.FindByIdResponse> response = controller.findById(1L);

        verify(service).findById(1L);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        UserController.FindByIdResponse body = response.getBody();
        assertThat(body.success, is(true));
        assertThat(body.error, is(nullValue()));
        assertThat(body.user, is(user));
    }

    @Test
    public void findByIdShouldReturnUserNullIfNotExists() {
        when(service.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<UserController.FindByIdResponse> response = controller.findById(1L);

        verify(service).findById(1L);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        UserController.FindByIdResponse body = response.getBody();
        assertThat(body.success, is(true));
        assertThat(body.error, is(nullValue()));
        assertThat(body.user, is(nullValue()));
    }

    @Test
    public void findByIdShouldCatchIllegalArgumentException() {
        doThrow(new IllegalArgumentException("message")).when(service).findById(1L);

        ResponseEntity<UserController.FindByIdResponse> response = controller.findById(1L);

        verify(service).findById(1L);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        UserController.FindByIdResponse body = response.getBody();
        assertThat(body.success, is(false));
        assertThat(body.error, is("message"));
        assertThat(body.user, is(nullValue()));
    }
}