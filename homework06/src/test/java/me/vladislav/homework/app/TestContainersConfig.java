package me.vladislav.homework.app;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.lifecycle.Startables;

@Slf4j
@ActiveProfiles("test")
@ContextConfiguration(initializers = TestContainersConfig.Initializer.class)
public class TestContainersConfig {
  private static final CassandraContainer<?> CASSANDRA =
      new CassandraContainer<>("cassandra:3.11.2");

  static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(@NotNull ConfigurableApplicationContext context) {
      Startables.deepStart(CASSANDRA).join();

      System.out.println("spring.cassandra.local-datacenter=datacenter1");
      System.out.println("spring.cassandra.contact-point=" + CASSANDRA.getHost());
      System.out.println("spring.cassandra.port=" + CASSANDRA.getMappedPort(9042));
      System.out.println("spring.cassandra.keyspace-name=test_keyspace");
      System.out.println("spring.cassandra.username=" + CASSANDRA.getUsername());
      System.out.println("spring.cassandra.password=" + CASSANDRA.getPassword());

      TestPropertyValues.of(
          "spring.cassandra.local-datacenter=datacenter1",
          "spring.cassandra.contact-point=" + CASSANDRA.getHost(),
          "spring.cassandra.port=" + CASSANDRA.getMappedPort(9042),
          "spring.cassandra.keyspace-name=test_keyspace",
          "spring.cassandra.username=" + CASSANDRA.getUsername(),
          "spring.cassandra.password=" + CASSANDRA.getPassword()
      ).applyTo(context);
    }
  }
}