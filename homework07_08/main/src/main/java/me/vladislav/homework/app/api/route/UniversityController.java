package me.vladislav.homework.app.api.route;

import io.github.resilience4j.ratelimiter.RateLimiter;
import lombok.RequiredArgsConstructor;
import me.vladislav.homework.app.api.route.annotation.UniversityControllerAnnotation;
import me.vladislav.homework.app.dto.api.request.UniversityCreateRequest;
import me.vladislav.homework.app.dto.api.response.UniversityGetResponse;
import me.vladislav.homework.app.service.UniversityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/{userId}/university")
public class UniversityController implements UniversityControllerAnnotation {
  private final UniversityService universityService;
  private final RateLimiter rateLimiter = RateLimiter.ofDefaults("UniversityControllerRateLimiter");

  @PostMapping
  public ResponseEntity<Void> addUniversityForUser(
      Long userId, UniversityCreateRequest university) {
    return rateLimiter.executeSupplier(() -> {
          universityService.addNewUniversityForUser(userId, university);
          return ResponseEntity.status(HttpStatus.CREATED).build();
        }
    );
  }

  @GetMapping
  public ResponseEntity<List<UniversityGetResponse>> getUniversitiesForUser(Long userId) {
    return rateLimiter.executeSupplier(() -> {
      return ResponseEntity.status(HttpStatus.OK)
          .body(
              universityService.getUniversitiesForUser(userId).stream()
                  .map(
                      university ->
                          new UniversityGetResponse(
                              university.getId(),
                              university.getName(),
                              university.getCity(),
                              university.getDescription(),
                              university.getRateKrutosty()))
                  .collect(Collectors.toList()));
    });
  }
}
