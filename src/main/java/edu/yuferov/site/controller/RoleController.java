package edu.yuferov.site.controller;

import edu.yuferov.site.model.Role;
import edu.yuferov.site.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/roles")
@CrossOrigin
public class RoleController extends ControllerBase {

    @Autowired
    private RoleService service;

    public class CreateResponse extends Response {
        public Long id;
    }

    public class GetAllResponse extends Response {
        public Iterable<Role> data;
    }

    public class FindByIdResponse extends Response {
        public Role role;
    }

    @PostMapping
    public ResponseEntity<CreateResponse> create(@RequestBody Role role) {
        return exec(new CreateResponse(), body -> {
            if (role.getId() != null) {
                throw new IllegalArgumentException("To create role id must be null");
            }
            service.save(role);
            body.id = role.getId();
        });
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> update(@PathVariable Long id, @RequestBody Role role) {
        return exec(new Response(), body -> {
            if (id == null) {
                throw new IllegalArgumentException("To update role specify id as path variable");
            }
            role.setId(id);
            service.save(role);
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
        return exec(new FindByIdResponse(), body -> body.role = service.findById(id).orElse(null));
    }
}
