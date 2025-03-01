package me.vladislav.homework02.app.api.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.vladislav.homework02.app.core.exception.db.repository.UserNotFoundException;
import me.vladislav.homework02.app.dto.api.request.UserCreateRequest;
import me.vladislav.homework02.app.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private UserService userService;

  @Test
  void createUserSuccessfully() throws Exception {
    UserCreateRequest request = new UserCreateRequest("testuser", "test@example.com");
    when(userService.createUser(any(UserCreateRequest.class))).thenReturn(1L);

    mockMvc
        .perform(
            post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(content().string("1"));
  }

  @Test
  void tryUnprocessableUser() throws Exception {
    UserCreateRequest request = new UserCreateRequest("", "invalid-email");

    mockMvc
        .perform(
            post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnprocessableEntity());
  }

  @Test
  void getNonExistingUser() throws Exception {
    Long userId = 812L;
    when(userService.getUserById(userId)).thenThrow(new UserNotFoundException());

    mockMvc.perform(get("/api/user/" + userId)).andExpect(status().isNotFound());
  }
}
