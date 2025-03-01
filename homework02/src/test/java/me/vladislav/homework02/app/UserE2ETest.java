package me.vladislav.homework02.app;

import jakarta.annotation.PostConstruct;
import me.vladislav.homework02.app.dto.api.request.UserCreateRequest;
import me.vladislav.homework02.app.dto.api.response.UserGetResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserE2ETest {

  @Autowired
  private TestRestTemplate restTemplate;

  @PostConstruct
  public void setUp() {
    restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
  }

  @Test
  public void testCreateAndGetUser() {
    UserCreateRequest userRequest = new UserCreateRequest("testuser", "test@example.com");

    ResponseEntity<Long> createResponse =
        restTemplate.postForEntity("/api/user", userRequest, Long.class);

    assertTrue(createResponse.getStatusCode().isSameCodeAs(HttpStatus.CREATED));
    Long userId = createResponse.getBody();
    assertNotNull(userId);

    ResponseEntity<UserGetResponse> getResponse =
        restTemplate.getForEntity("/api/user/" + userId, UserGetResponse.class);

    assertTrue(getResponse.getStatusCode().is2xxSuccessful());
    UserGetResponse user = getResponse.getBody();
    assertNotNull(user);
    assertEquals(userRequest.username(), user.username());
    assertEquals(userRequest.email(), user.email());
  }

  @Test
  public void testGetNonExistentUserReturns404() {
    ResponseEntity<String> response =
        restTemplate.getForEntity("/api/user/812", String.class);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
}
