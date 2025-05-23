package me.vladislav.homework.app.api.route.broker.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.homework.app.dto.broker.UserAuditData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditProducer {
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  @Value("${topic-to-send-message}")
  String topic;

  public void sendMessage(UserAuditData messageDto) {
    String message;
    try {
      message = objectMapper.writeValueAsString(messageDto);
    } catch (JsonProcessingException e) {
      log.error("Error while serializing message", e);
      return;
    }

    CompletableFuture<SendResult<String, String>> sendResult =
        kafkaTemplate.send(topic, messageDto.userId().toString(), message);
  }
}
