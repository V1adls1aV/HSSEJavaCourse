package me.vladislav.homework02.app.api.route;

import lombok.RequiredArgsConstructor;
import me.vladislav.homework02.app.api.route.annotation.UniversityControllerAnnotation;
import me.vladislav.homework02.app.dto.api.request.UniversityCreateRequest;
import me.vladislav.homework02.app.dto.api.response.UniversityGetResponse;
import me.vladislav.homework02.app.service.UniversityService;
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

  @PostMapping
  public ResponseEntity<Void> addUniversityForUser(
      Long userId, UniversityCreateRequest university) {
    universityService.addNewUniversityForUser(userId, university);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping
  public ResponseEntity<List<UniversityGetResponse>> getUniversitiesForUser(Long userId) {
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
