package me.vladislav.homework.app;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;

@ActiveProfiles("test")
@Testcontainers
@ContextConfiguration(initializers = TestContainersManager.Initializer.class)
public class TestContainersManager {

  @Container
  protected static final CassandraContainer<?> CASSANDRA =
      new CassandraContainer<>("cassandra:4.1.9")
          .withExposedPorts(9042)
          .withInitScript("init-cassandra.cql")
          .withReuse(true);


  static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext context) {
      Startables.deepStart(CASSANDRA).join();

      TestPropertyValues.of(
          "spring.cassandra.contact-points=" + CASSANDRA.getHost(),
          "spring.cassandra.port=" + CASSANDRA.getMappedPort(9042),
          "spring.cassandra.local-datacenter=datacenter1",
          "spring.cassandra.keyspace-name=homework_keyspace"
      ).applyTo(context);
    }
  }
}
