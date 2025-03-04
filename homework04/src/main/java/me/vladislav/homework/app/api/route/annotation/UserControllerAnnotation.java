package me.vladislav.homework.app.api.route.annotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import me.vladislav.homework.app.dto.api.request.UserCreateRequest;
import me.vladislav.homework.app.dto.api.response.UserGetResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@Tag(name = "User management")
public interface UserControllerAnnotation {

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
  ResponseEntity<Long> createUser(@Parameter(description = "User's profile data") @Valid @RequestBody UserCreateRequest request);

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
  ResponseEntity<UserGetResponse> getUser(@PathVariable Long userId);
}
