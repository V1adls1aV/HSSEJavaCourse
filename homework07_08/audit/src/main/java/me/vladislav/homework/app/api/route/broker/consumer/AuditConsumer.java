package me.vladislav.homework.app.api.route.broker.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.homework.app.dto.UserAuditData;
import me.vladislav.homework.app.service.UserAuditService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuditConsumer {
  private final ObjectMapper objectMapper;
  private final UserAuditService userAuditService;

  @Value("${topic-to-consume-message}")
  String topic;

  @KafkaListener(topics = {"${topic-to-consume-message}"})
  public void consumeMessage(String message) {
    UserAuditData parsedMessage;
    try {
      parsedMessage = objectMapper.readValue(message, UserAuditData.class);
    } catch (JsonProcessingException e) {
      log.error("Failed to deserialize the message from topic={}", topic, e);
      return;
    }

    log.debug("Consume the message from topic={} from userId={}", topic, parsedMessage.userId());
    userAuditService.recordUserAction(parsedMessage);
  }
}
