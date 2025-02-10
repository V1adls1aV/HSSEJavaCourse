package me.vladislav.homework01.app.api.route;

import java.util.List;
import java.util.stream.Collectors;
import me.vladislav.homework01.app.dto.api.request.UniversityCreateRequest;
import me.vladislav.homework01.app.dto.api.response.UniversityGetResponse;
import me.vladislav.homework01.app.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/university")
public class UniversityHandler {
  private final UniversityService universityService;

  @Autowired
  public UniversityHandler(UniversityService universityService) {
    this.universityService = universityService;
  }

  @PutMapping("/user/{userId}")
  public void addUniversityForUser(
      @PathVariable Long userId, @RequestBody UniversityCreateRequest university) {
    universityService.addNewUniversityForUser(userId, university);
  }

  @GetMapping("/user/{userId}")
  public List<UniversityGetResponse> getUniversitiesForUser(@PathVariable Long userId) {
    return universityService.getUniversitiesForUser(userId).stream()
        .map(university -> new UniversityGetResponse(
            university.id(),
            university.name(),
            university.city(),
            university.description(),
            university.rateKrutosty()))
        .collect(Collectors.toList());
  }
}
