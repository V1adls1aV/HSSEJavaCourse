package me.vladislav.homework01.app.api.route;

import me.vladislav.homework01.app.dto.api.request.UserCreateRequest;
import me.vladislav.homework01.app.dto.api.response.UserGetResponse;
import me.vladislav.homework01.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserHandler {
  private final UserService userService;

  @Autowired
  public UserHandler(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/")
  public Long createUser(@RequestBody UserCreateRequest request) {
    return userService.createUser(request);
  }

  @GetMapping("/{userId}")
  public UserGetResponse getUser(@PathVariable Long userId) {
    var user = userService.getUserById(userId);
    return new UserGetResponse(user.id(), user.username(), user.email());
  }
}
