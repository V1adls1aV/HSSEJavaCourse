package me.vladislav.homework.app;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.util.Map;

@Configuration
public class CassandraConfiguration {

  @Bean
  public CqlSession cqlSession(CqlSessionBuilder sessionBuilder) {
    InetSocketAddress address = InetSocketAddress.createUnresolved("127.0.0.1", 9042);
    sessionBuilder = sessionBuilder.addContactPoint(address);
    sessionBuilder.withKeyspace((CqlIdentifier) null);

    CqlSession session = sessionBuilder.build();

    SimpleStatement statement = SchemaBuilder.createKeyspace("homework_keyspace")
            .ifNotExists()
            .withNetworkTopologyStrategy(Map.of("datacenter1", 1))
            .build();
    session.execute(statement);

    session.execute("""
            CREATE TABLE IF NOT EXISTS homework_keyspace.user_audit (
                user_id BIGINT,
                perform_time TIMESTAMP,
                operation_type TEXT,
                detail TEXT,
                PRIMARY KEY ((user_id), perform_time)
            ) WITH CLUSTERING ORDER BY (perform_time DESC)
               AND default_time_to_live = 2592000;
            """);

    return sessionBuilder
            .withKeyspace("homework_keyspace")
            .build();
  }
}