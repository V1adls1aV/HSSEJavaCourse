package me.vladislav.homework01.app.api.route;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import me.vladislav.homework01.app.dto.api.request.UniversityCreateRequest;
import me.vladislav.homework01.app.dto.api.response.UniversityGetResponse;
import me.vladislav.homework01.app.service.UniversityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/university")
public class UniversityHandler {
  private final UniversityService universityService;

  public UniversityHandler(UniversityService universityService) {
    this.universityService = universityService;
  }

  @PutMapping("/user/{userId}")
  public ResponseEntity<Void> addUniversityForUser(
      @PathVariable Long userId, @Valid @RequestBody UniversityCreateRequest university) {
    universityService.addNewUniversityForUser(userId, university);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/user/{userId}")
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
