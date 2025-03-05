package me.vladislav.homework.app.core.aspect;

import lombok.RequiredArgsConstructor;
import me.vladislav.homework.app.api.route.UniversityController;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoggingAspectTest {
  private UniversityController universityController;
  private LoggingAspect loggingAspect;

  @Test
  public void universityController() {
    assertEquals(0L, loggingAspect.getCallsCount());

    universityController.getUniversitiesForUser(812L);

    assertEquals(2L, loggingAspect.getCallsCount());
  }
}
