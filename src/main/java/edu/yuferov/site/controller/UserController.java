package edu.yuferov.site.controller;

import edu.yuferov.site.model.Role;
import edu.yuferov.site.model.User;
import edu.yuferov.site.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController extends ControllerBase {

    /// Requests
    public static class CreateUpdateRequest {
        public String name;
        public Long roleId;
    }

    /// Responses
    public class CreateResponse extends Response {
        public Long id;
    }

    public class GetAllResponse extends Response {
        public Iterable<User> data;
    }

    public class FindByIdResponse extends Response {
        public User user;
    }

    @Autowired
    private UserService service;

    @PostMapping
    public ResponseEntity<CreateResponse> create(@RequestBody CreateUpdateRequest request) {
        return exec(new CreateResponse(), body -> {
            User user = new User(request.name, new Role(request.roleId));
            service.save(user);
            body.id = user.getId();
        });
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> update(@PathVariable Long id, @RequestBody CreateUpdateRequest request) {
        return exec(new Response(), body -> {
            if (id == null) {
                throw new IllegalArgumentException("To update user specify id as path variable");
            }
            User user = new User(request.name, new Role(request.roleId));
            user.setId(id);
            service.save(user);
        });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable Long id) {
        return exec(new Response(), body -> service.delete(id));
    }

    @GetMapping
    public ResponseEntity<GetAllResponse> getAll() {
        return exec(new GetAllResponse(), body -> body.data = service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindByIdResponse> findById(@PathVariable Long id) {
        return exec(new FindByIdResponse(), body -> body.user = service.findById(id).orElse(null));
    }
}
