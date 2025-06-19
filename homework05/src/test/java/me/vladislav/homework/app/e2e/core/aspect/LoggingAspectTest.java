package me.vladislav.homework.app.e2e.core.aspect;

import me.vladislav.homework.app.TestContainersConfig;
import me.vladislav.homework.app.core.aspect.LoggingAspect;
import me.vladislav.homework.app.dto.api.request.UserCreateRequest;
import me.vladislav.homework.app.dto.api.response.UserGetResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoggingAspectTest extends TestContainersConfig {
  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private LoggingAspect loggingAspect;

  @Test
  public void testUserController() {
    long defaultCallsCount = loggingAspect.getCallsCount();

    UserCreateRequest userRequest = new UserCreateRequest("testuser", "test@example.com");
    ResponseEntity<Long> createResponse =
        restTemplate.postForEntity("/api/user", userRequest, Long.class);

    assertTrue(createResponse.getStatusCode().isSameCodeAs(HttpStatus.CREATED));
    Long userId = createResponse.getBody();
    assertNotNull(userId);

    assertEquals(2L, loggingAspect.getCallsCount() - defaultCallsCount);

    ResponseEntity<UserGetResponse> userResponse =
        restTemplate.getForEntity("/api/user/" + userId, UserGetResponse.class);
    assertTrue(userResponse.getStatusCode().is2xxSuccessful());

    assertEquals(4L, loggingAspect.getCallsCount() - defaultCallsCount);
  }
}
