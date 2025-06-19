package me.vladislav.homework.app.core.web;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {
  private final WebProperties webProperties;

  @Bean
  public RestTemplate restTemplate() {
    SimpleClientHttpRequestFactory factory = new
        SimpleClientHttpRequestFactory();
    factory.setConnectTimeout(webProperties.getTimeout());
    factory.setReadTimeout(webProperties.getTimeout());
    return new RestTemplate(factory);
  }
}
