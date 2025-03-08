package me.vladislav.homework.app.api.route.actuator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Endpoint(id = "uuid")
public class RandomUuidController {
  @ReadOperation
  public UUID randomUuid() {
    return UUID.randomUUID();
  }
}
