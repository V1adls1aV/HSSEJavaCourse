package me.vladislav.homework.app.api.route.annotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import me.vladislav.homework.app.dto.api.request.BookCreateRequest;
import me.vladislav.homework.app.dto.api.request.BookPatchRequest;
import me.vladislav.homework.app.dto.api.request.BookUpdateRequest;
import me.vladislav.homework.app.dto.api.response.BookGetResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Book management")
public interface BookControllerAnnotation {


  @Operation(summary = "Get all books for user")
  @ApiResponse(
      responseCode = "200",
      description = "Books retrieved successfully",
      content = @Content(schema = @Schema(implementation = BookGetResponse.class)))
  @ApiResponse(responseCode = "404", description = "User not found")
  ResponseEntity<List<BookGetResponse>> getBooksForUser(
      @Parameter(description = "User ID") @PathVariable Long userId);

  @Operation(summary = "Add new book for user")
  @ApiResponse(
      responseCode = "201",
      description = "Book created successfully",
      content = @Content(schema = @Schema(implementation = BookGetResponse.class)))
  @ApiResponse(responseCode = "404", description = "User not found")
  @ApiResponse(responseCode = "422", description = "Invalid input")
  ResponseEntity<BookGetResponse> addBookForUser(
      @Parameter(description = "User ID") @PathVariable Long userId,
      @Parameter(description = "Book data") @Valid @RequestBody BookCreateRequest book);

  @Operation(summary = "Update book for user. If book does not exist, creates it and returns the actual book with supplied content.")
  @ApiResponse(
      responseCode = "200",
      description = "Book updated successfully",
      content = @Content(schema = @Schema(implementation = BookGetResponse.class)))
  @ApiResponse(responseCode = "404", description = "User not found")
  @ApiResponse(responseCode = "422", description = "Invalid input")
  ResponseEntity<BookGetResponse> updateBookForUser(
      @Parameter(description = "User ID") @PathVariable Long userId,
      @Parameter(description = "Book data") @Valid @RequestBody BookUpdateRequest book);

  @Operation(summary = "Partially update book for user")
  @ApiResponse(
      responseCode = "200",
      description = "Book partially updated successfully",
      content = @Content(schema = @Schema(implementation = BookGetResponse.class)))
  @ApiResponse(responseCode = "404", description = "User or book not found")
  @ApiResponse(responseCode = "422", description = "Invalid input")
  ResponseEntity<BookGetResponse> partiallyUpdateBookForUser(
      @Parameter(description = "User ID") @PathVariable Long userId,
      @Parameter(description = "Book data") @Valid @RequestBody BookPatchRequest book);

  @Operation(summary = "Delete book for user")
  @ApiResponse(
      responseCode = "200",
      description = "Book deleted successfully",
      content = @Content(schema = @Schema(implementation = BookGetResponse.class)))
  @ApiResponse(responseCode = "404", description = "User or book not found")
  ResponseEntity<BookGetResponse> deleteBookForUser(
      @Parameter(description = "User ID") @PathVariable Long userId,
      @Parameter(description = "Book ID") @PathVariable Long bookId);
}
