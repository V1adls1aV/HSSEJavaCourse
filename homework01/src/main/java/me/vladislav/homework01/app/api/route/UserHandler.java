package me.vladislav.homework01.app.api.route;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import me.vladislav.homework01.app.dto.api.request.UserCreateRequest;
import me.vladislav.homework01.app.dto.api.response.UserGetResponse;
import me.vladislav.homework01.app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "User management")
@RestController
@RequestMapping("/api/user")
public class UserHandler {
  private final UserService userService;

  public UserHandler(UserService userService) {
    this.userService = userService;
  }

  @Operation(summary = "Create new user")
  @ApiResponse(
      responseCode = "201",
      description = "User created successfully",
      content = @Content(schema = @Schema(implementation = Long.class))
  )
  @ApiResponse(
      responseCode = "422",
      description = "Invalid input"
  )
  @PostMapping("/")
  public ResponseEntity<Long> createUser(@Valid @RequestBody UserCreateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
  }

  @Operation(summary = "Get user by ID")
  @ApiResponse(
      responseCode = "200",
      description = "User found",
      content = @Content(schema = @Schema(implementation = UserGetResponse.class))
  )
  @ApiResponse(
      responseCode = "404",
      description = "User not found"
  )
  @GetMapping("/{userId}")
  public ResponseEntity<UserGetResponse> getUser(@PathVariable Long userId) {
    var user = userService.getUserById(userId);
    return ResponseEntity.status(HttpStatus.OK).body(new UserGetResponse(user.id(), user.username(), user.email()));
  }
}
