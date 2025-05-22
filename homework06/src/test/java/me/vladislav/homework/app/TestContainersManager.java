package me.vladislav.homework.app;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@ActiveProfiles("test")
@Testcontainers
public class TestContainersManager {

  @Container
  private static final CassandraContainer<?> CASSANDRA =
      new CassandraContainer<>("cassandra:4.1.9").withExposedPorts(9042);

  @BeforeAll
  static void setupCassandraConnectionProperties() {
    System.setProperty("spring.cassandra.port", String.valueOf(CASSANDRA.getMappedPort(9042)));
  }
}