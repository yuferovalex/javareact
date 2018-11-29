package edu.yuferov.site.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private Role role;

    public User(Long id) {
        this.id = id;
    }

    public User(String name, Role role) {
        this.name = name;
        this.role = role;
    }
}
