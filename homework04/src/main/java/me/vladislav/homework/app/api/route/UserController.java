package me.vladislav.homework.app.api.route;

import lombok.RequiredArgsConstructor;
import me.vladislav.homework.app.api.route.annotation.UserControllerAnnotation;
import me.vladislav.homework.app.core.exception.db.repository.UserNotFoundException;
import me.vladislav.homework.app.dto.api.request.UserCreateRequest;
import me.vladislav.homework.app.dto.api.response.UserGetResponse;
import me.vladislav.homework.app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController implements UserControllerAnnotation {
  private final UserService userService;

  @PostMapping
  public ResponseEntity<Long> createUser(UserCreateRequest request)
      throws ExecutionException, InterruptedException {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request).get());
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserGetResponse> getUser(Long userId)
      throws ExecutionException, InterruptedException {
    try {
      var user = userService.getUserById(userId).get();
      return ResponseEntity.status(HttpStatus.OK)
          .body(new UserGetResponse(user.id(), user.username(), user.email()));
    } catch (ExecutionException e) {
      if (e.getCause() instanceof UserNotFoundException) {
        throw (UserNotFoundException) e.getCause();
      }
      throw e;
    }
  }
}
