package me.vladislav.homework.app;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

@ActiveProfiles("test")
@ContextConfiguration(initializers = TestContainersConfig.Initializer.class)
public class TestContainersConfig {
  private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

  static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
      Startables.deepStart(POSTGRES).join();

      TestPropertyValues.of(
          "spring.datasource.url=" + POSTGRES.getJdbcUrl(),
          "spring.datasource.username=" + POSTGRES.getUsername(),
          "spring.datasource.password=" + POSTGRES.getPassword()
      ).applyTo(context);
    }
  }
}