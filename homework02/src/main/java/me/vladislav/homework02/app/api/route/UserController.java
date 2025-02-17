package me.vladislav.homework02.app.api.route;

import me.vladislav.homework02.app.api.route.annotation.UserControllerAnnotation;
import me.vladislav.homework02.app.dto.api.request.UserCreateRequest;
import me.vladislav.homework02.app.dto.api.response.UserGetResponse;
import me.vladislav.homework02.app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
public class UserController implements UserControllerAnnotation {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public ResponseEntity<Long> createUser(UserCreateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserGetResponse> getUser(Long userId) {
    var user = userService.getUserById(userId);
    return ResponseEntity.status(HttpStatus.OK).body(new UserGetResponse(user.id(), user.username(), user.email()));
  }
}
