package me.vladislav.homework.app;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

@ActiveProfiles("test")
@Testcontainers
@ContextConfiguration(initializers = TestContainersManager.Initializer.class)
public class TestContainersManager {

  protected static final CassandraContainer<?> CASSANDRA =
      new CassandraContainer<>("cassandra:4.1.9")
          .withInitScript("init-cassandra.cql")
          .withReuse(true);

  protected static final KafkaContainer KAFKA =
      new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0")).withReuse(true);

  static class Initializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext context) {
      Startables.deepStart(CASSANDRA).join();
      Startables.deepStart(KAFKA).join();

      TestPropertyValues.of(
              "spring.cassandra.contact-points=" + CASSANDRA.getHost(),
              "spring.cassandra.port=" + CASSANDRA.getMappedPort(9042),
              "spring.cassandra.local-datacenter=datacenter1",
              "spring.cassandra.keyspace-name=homework_keyspace",
              "spring.kafka.bootstrap-servers=" + KAFKA.getBootstrapServers(),
              "spring.kafka.consumer.group-id=homework-group")
          .applyTo(context);
    }
  }
}
