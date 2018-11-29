package edu.yuferov.site.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Role {
    private Long id;
    private String title;
    private String description;

    public Role(Long id) {
        this.id = id;
    }

    public Role(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
