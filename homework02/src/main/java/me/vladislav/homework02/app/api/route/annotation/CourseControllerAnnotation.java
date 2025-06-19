package me.vladislav.homework02.app.api.route.annotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import me.vladislav.homework02.app.dto.api.request.CourseCreateRequest;
import me.vladislav.homework02.app.dto.api.request.CoursePatchRequest;
import me.vladislav.homework02.app.dto.api.request.CourseUpdateRequest;
import me.vladislav.homework02.app.dto.api.response.CourseGetResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Course management")
public interface CourseControllerAnnotation {

  @Operation(summary = "Add new course for user")
  @ApiResponse(
      responseCode = "201",
      description = "Course created successfully",
      content = @Content(schema = @Schema(implementation = CourseGetResponse.class)))
  @ApiResponse(responseCode = "404", description = "User not found")
  @ApiResponse(responseCode = "422", description = "Invalid input")
  ResponseEntity<CourseGetResponse> addCourseForUser(
      @Parameter(description = "User ID") @PathVariable Long userId,
      @Parameter(description = "Course data") @Valid @RequestBody CourseCreateRequest course);

  @Operation(summary = "Get all courses for user")
  @ApiResponse(
      responseCode = "200",
      description = "Courses retrieved successfully",
      content = @Content(schema = @Schema(implementation = CourseGetResponse.class)))
  @ApiResponse(responseCode = "404", description = "User not found")
  ResponseEntity<List<CourseGetResponse>> getCoursesForUser(
      @Parameter(description = "User ID") @PathVariable Long userId);

  @Operation(summary = "Update course for user")
  @ApiResponse(
      responseCode = "200",
      description = "Course updated successfully",
      content = @Content(schema = @Schema(implementation = CourseGetResponse.class)))
  @ApiResponse(responseCode = "404", description = "User or course not found")
  @ApiResponse(responseCode = "422", description = "Invalid input")
  ResponseEntity<CourseGetResponse> updateCourseForUser(
      @Parameter(description = "User ID") @PathVariable Long userId,
      @Parameter(description = "Course data") @Valid @RequestBody CourseUpdateRequest course);

  @Operation(summary = "Partially update course for user")
  @ApiResponse(
      responseCode = "200",
      description = "Course partially updated successfully",
      content = @Content(schema = @Schema(implementation = CourseGetResponse.class)))
  @ApiResponse(responseCode = "404", description = "User or course not found")
  @ApiResponse(responseCode = "422", description = "Invalid input")
  ResponseEntity<CourseGetResponse> partiallyUpdateCourseForUser(
      @Parameter(description = "User ID") @PathVariable Long userId,
      @Parameter(description = "Course data") @Valid @RequestBody CoursePatchRequest course);

  @Operation(summary = "Delete course for user")
  @ApiResponse(
      responseCode = "200",
      description = "Course deleted successfully",
      content = @Content(schema = @Schema(implementation = CourseGetResponse.class)))
  @ApiResponse(responseCode = "404", description = "User or course not found")
  ResponseEntity<CourseGetResponse> deleteCourseForUser(
      @Parameter(description = "User ID") @PathVariable Long userId,
      @Parameter(description = "Course ID") @PathVariable Long courseId);
}
