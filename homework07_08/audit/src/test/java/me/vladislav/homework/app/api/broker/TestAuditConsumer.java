package me.vladislav.homework.app.api.broker;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import me.vladislav.homework.app.TestContainersManager;
import me.vladislav.homework.app.dto.OperationType;
import me.vladislav.homework.app.dto.UserAuditData;
import me.vladislav.homework.app.service.UserAuditService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({KafkaAutoConfiguration.class})
@Testcontainers
public class TestAuditConsumer extends TestContainersManager {

  private final Long userId = 728L;

  @Value("${topic-to-consume-message}")
  String topic;

  @Autowired private KafkaTemplate<String, String> kafkaTemplate;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private UserAuditService userAuditService;

  @Test
  void shouldSendMessageToKafkaSuccessfully() throws JsonProcessingException {
    LocalDateTime fixedTime = LocalDateTime.of(2025, 5, 22, 17, 41, 57, 0);
    kafkaTemplate.send(
        topic,
        objectMapper.writeValueAsString(
            new UserAuditData(
                userId, fixedTime.plusMinutes(1), OperationType.CREATE, "test create")));

    kafkaTemplate.send(
        topic,
        objectMapper.writeValueAsString(
            new UserAuditData(userId, fixedTime, OperationType.UPDATE, "test update")));

    await()
        .atMost(Duration.ofSeconds(5))
        .pollDelay(Duration.ofSeconds(1))
        .untilAsserted(
            () -> {
              List<UserAuditData> result = userAuditService.getUserAuditData(userId, 5);

              assertEquals(2, result.size());

              assertEquals(OperationType.CREATE, result.get(0).operationType());
              assertEquals(OperationType.UPDATE, result.get(1).operationType());
            });
  }
}
