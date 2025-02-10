package me.vladislav.homework01.app.api.route;

import me.vladislav.homework01.app.dto.api.request.UserCreateRequest;
import me.vladislav.homework01.app.dto.api.response.UserGetResponse;
import me.vladislav.homework01.app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserHandler {
  private final UserService userService;

  public UserHandler(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/")
  public ResponseEntity<Long> createUser(@RequestBody UserCreateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserGetResponse> getUser(@PathVariable Long userId) {
    var user = userService.getUserById(userId);
    return ResponseEntity.status(HttpStatus.OK).body(new UserGetResponse(user.id(), user.username(), user.email()));
  }
}
