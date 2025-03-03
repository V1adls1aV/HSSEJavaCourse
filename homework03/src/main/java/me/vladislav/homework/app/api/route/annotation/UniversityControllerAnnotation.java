package me.vladislav.homework.app.api.route.annotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import me.vladislav.homework.app.dto.api.request.UniversityCreateRequest;
import me.vladislav.homework.app.dto.api.response.UniversityGetResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "University management")
public interface UniversityControllerAnnotation {

  @Operation(summary = "Add new university for user")
  @ApiResponse(responseCode = "201", description = "University created successfully")
  @ApiResponse(responseCode = "404", description = "User not found")
  @ApiResponse(responseCode = "422", description = "Invalid input")
  ResponseEntity<Void> addUniversityForUser(
      @Parameter(description = "User ID") @PathVariable Long userId,
      @Parameter(description = "University data") @Valid @RequestBody
      UniversityCreateRequest university);

  @Operation(summary = "Get all universities for user")
  @ApiResponse(
      responseCode = "200",
      description = "Universities retrieved successfully",
      content = @Content(schema = @Schema(implementation = UniversityGetResponse.class)))
  @ApiResponse(responseCode = "404", description = "User not found")
  ResponseEntity<List<UniversityGetResponse>> getUniversitiesForUser(
      @Parameter(description = "User ID") @PathVariable Long userId);
}
