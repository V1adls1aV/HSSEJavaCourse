package me.vladislav.homework.app.core.cache;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cache")
public class CacheProperties {
  private int expireAfterSeconds = 60;
  private int initialCapacity = 10;
}

