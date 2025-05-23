package me.vladislav.homework.app.e2e.broker;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.util.List;
import me.vladislav.homework.app.TestContainersConfig;
import me.vladislav.homework.app.api.route.broker.producer.AuditProducer;
import me.vladislav.homework.app.dto.api.request.BookCreateRequest;
import me.vladislav.homework.app.dto.api.request.UserCreateRequest;
import me.vladislav.homework.app.dto.api.response.BookGetResponse;
import me.vladislav.homework.app.dto.broker.OperationType;
import me.vladislav.homework.app.dto.broker.UserAuditData;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(classes = {AuditProducer.class, TestRestTemplate.class})
@Import({KafkaAutoConfiguration.class, AuditTest.ObjectMapperTestConfig.class})
@Testcontainers
class AuditTest extends TestContainersConfig {

  @Value("${topic-to-send-message}")
  String topic;

  @Autowired private ObjectMapper objectMapper;
  @Autowired private TestRestTemplate restTemplate;

  @PostConstruct
  public void setUp() {
    restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
  }

  @Test
  void shouldSendMessageToKafkaSuccessfully() {
    // Create a user first
    UserCreateRequest userRequest = new UserCreateRequest("bookuser", "books@test.com");
    ResponseEntity<Long> createUserResponse =
        restTemplate.postForEntity("/api/user", userRequest, Long.class);
    assertTrue(createUserResponse.getStatusCode().isSameCodeAs(HttpStatus.CREATED));
    Long userId = createUserResponse.getBody();
    assertNotNull(userId);

    // Create a book
    BookCreateRequest createRequest = new BookCreateRequest("1984", "George");
    ResponseEntity<BookGetResponse> createBookResponse =
        restTemplate.postForEntity(
            "/api/user/" + userId + "/book", createRequest, BookGetResponse.class);
    assertTrue(createBookResponse.getStatusCode().isSameCodeAs(HttpStatus.CREATED));

    // Check the message sent
    KafkaTestConsumer consumer =
        new KafkaTestConsumer(KAFKA.getBootstrapServers(), "homework-group");
    consumer.subscribe(List.of(topic));

    ConsumerRecords<String, String> records = consumer.poll();
    Assertions.assertEquals(1, records.count());
    records
        .iterator()
        .forEachRemaining(
            record -> {
              UserAuditData message;
              try {
                message = objectMapper.readValue(record.value(), UserAuditData.class);
              } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
              }

              // Check the userId remained the same, operation type too.
              Assertions.assertEquals(userId, message.userId());
              Assertions.assertEquals(OperationType.CREATE, message.operationType());
            });
  }

  @TestConfiguration
  static class ObjectMapperTestConfig {
    @Bean
    public ObjectMapper objectMapper() {
      return new ObjectMapper();
    }
  }
}
