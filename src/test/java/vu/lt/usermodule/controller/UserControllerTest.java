package vu.lt.usermodule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import vu.lt.usermodule.controller.UserController;
import vu.lt.usermodule.model.User;
import vu.lt.usermodule.service.UserService;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserService userService;

    private static final List<User> users = new ArrayList<>();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @AfterEach
    void tearDown() {
        reset(userService);
    }

    @BeforeAll
    private static void beforeAll() {
        for (int i = 0; i < 5; i++) {
            users.add(new User());
        }
    }

    @Test
    void givenWac_whenServletContext_thenItProvidesController() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(webApplicationContext.getBean("userController"));
    }

    @Test
    void getAllUsers_performGetRequest_findsAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(users);

        MvcResult result = mockMvc.perform(get("/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        verify(userService).getAllUsers();

        assertThat(result.getResponse().getContentAsString())
                .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(users));
    }

    @Test
    void getUserById_performGetRequest_findsSingleUser() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(users.get(1));

        MvcResult result = mockMvc.perform(get("/users/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        verify(userService).getUserById(anyLong());

        assertThat(result.getResponse().getContentAsString())
                .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(users.get(1)));
    }

    @Test
    void saveUser_performPostRequest_savesUser() throws Exception {
        when(userService.saveUser(any(User.class))).thenReturn(users.get(1));

        MvcResult result = mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(new User()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        verify(userService).saveUser(any(User.class));

        assertThat(result.getResponse().getContentAsString())
                .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(users.get(1)));
    }

    @Test
    void deleteUser_performDeleteRequest_deletesUser() throws Exception {
        mockMvc.perform(delete("/users/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(userService).deleteUser(anyLong());
    }
}
