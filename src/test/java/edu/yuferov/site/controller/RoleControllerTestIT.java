package edu.yuferov.site.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = { "classpath:schema.sql", "classpath:data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class RoleControllerTestIT {

    private final String roleControllerUrl = "http://localhost:8080/roles";

    @Autowired
    private MockMvc mvc;

    //@Autowired
    //private DataSource dataSource;

    @Test
    public void shouldCreateRole() throws Exception {
        String requestBody = "{ `title`: `some_title`, `description`: `some_description` }"
                .replace('`', '"');

        mvc.perform(post(roleControllerUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andExpect(jsonPath("$.success", is(true)));
    }

    @Test
    public void shouldUpdateRole() throws Exception {
        String requestBody = "{ `title`: `some_title`, `description`: `some_description` }"
                .replace('`', '"');

        mvc.perform(put(roleControllerUrl + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andExpect(jsonPath("$.success", is(true)));
    }

    @Test
    public void shouldRemoveRole() throws Exception {
        mvc.perform(delete(roleControllerUrl + "/4")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andExpect(jsonPath("$.success", is(true)));
    }

    @Test
    public void shouldGetAllRoles() throws Exception {
        mvc.perform(get(roleControllerUrl)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(4)));
    }

    @Test
    public void shouldFindById() throws Exception {
        mvc.perform(get(roleControllerUrl + "/1")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.role", is(notNullValue())))
                .andExpect(jsonPath("$.role.id", is(1)))
                .andExpect(jsonPath("$.role.title", is("Роль #1")))
                .andExpect(jsonPath("$.role.description", is("Описание роли #1")));
    }

}