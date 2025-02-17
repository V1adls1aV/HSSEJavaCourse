package me.vladislav.homework02.app.api.route;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import me.vladislav.homework02.app.dto.api.request.UniversityCreateRequest;
import me.vladislav.homework02.app.dto.api.response.UniversityGetResponse;
import me.vladislav.homework02.app.service.UniversityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/{userId}/university")
public class UniversityHandler {
  private final UniversityService universityService;

  public UniversityHandler(UniversityService universityService) {
    this.universityService = universityService;
  }

  @PostMapping
  public ResponseEntity<Void> addUniversityForUser(
      @PathVariable Long userId, @Valid @RequestBody UniversityCreateRequest university) {
    universityService.addNewUniversityForUser(userId, university);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping
  public ResponseEntity<List<UniversityGetResponse>> getUniversitiesForUser(
      @PathVariable Long userId) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(
            universityService.getUniversitiesForUser(userId).stream()
                .map(
                    university ->
                        new UniversityGetResponse(
                            university.id(),
                            university.name(),
                            university.city(),
                            university.description(),
                            university.rateKrutosty()))
                .collect(Collectors.toList()));
  }
}
