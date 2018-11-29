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

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = { "classpath:schema.sql", "classpath:data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserControllerTestIT {

    private final String userControllerUrl = "http://localhost:8080/users";

    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldCreateUser() throws Exception {
        String requestBody = "{ `name`: `Some Name`, `roleId`: `1` }"
                .replace('`', '"');

        mvc.perform(post(userControllerUrl)
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
    public void shouldUpdateUser() throws Exception {
        String requestBody = "{ `name`: `Some Name`, `roleId`: `1` }"
                .replace('`', '"');

        mvc.perform(put(userControllerUrl + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andExpect(jsonPath("$.success", is(true)));
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        mvc.perform(delete(userControllerUrl + "/1")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andExpect(jsonPath("$.success", is(true)));
    }

    @Test
    public void getAll() throws Exception {
        mvc.perform(get(userControllerUrl)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(4)));
    }

    @Test
    public void findById() throws Exception {
        mvc.perform(get(userControllerUrl + "/1")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.user", is(notNullValue())))
                .andExpect(jsonPath("$.user.id", is(1)))
                .andExpect(jsonPath("$.user.name", is("Вася Пупкин")))
                .andExpect(jsonPath("$.user.role", is(notNullValue())))
                .andExpect(jsonPath("$.user.role.id", is(1)))
                .andExpect(jsonPath("$.user.role.title", is("Роль #1")))
                .andExpect(jsonPath("$.user.role.description", is("Описание роли #1")));
    }

}