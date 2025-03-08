package me.vladislav.homework.app.api.route;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.CompletableFuture;
import me.vladislav.homework.app.core.exception.db.repository.UserNotFoundException;
import me.vladislav.homework.app.dto.api.request.UserCreateRequest;
import me.vladislav.homework.app.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = UserController.class,
    excludeAutoConfiguration = {DataSourceAutoConfiguration.class, SecurityAutoConfiguration.class})
@ActiveProfiles("test")
class UserControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private UserService userService;

  @Test
  void createUserSuccessfully() throws Exception {
    UserCreateRequest request = new UserCreateRequest("testuser", "test@example.com");
    when(userService.createUser(any(UserCreateRequest.class)))
        .thenReturn(CompletableFuture.completedFuture(1L));

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
    when(userService.getUserById(userId))
        .thenReturn(CompletableFuture.failedFuture(new UserNotFoundException()));

    mockMvc.perform(get("/api/user/" + userId)).andExpect(status().isNotFound());
  }
}
