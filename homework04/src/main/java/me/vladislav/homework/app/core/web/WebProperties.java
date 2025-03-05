package me.vladislav.homework.app.core.web;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "requests")
public class WebProperties {
  private int timeout = 5000;
}
