CREATE KEYSPACE IF NOT EXISTS homework_keyspace
WITH replication = {'class': 'NetworkTopologyStrategy', 'datacenter1': 1};

USE homework_keyspace;

CREATE TABLE IF NOT EXISTS user_audit (
    user_id BIGINT,
    perform_time TIMESTAMP,
    operation_type TEXT,
    detail TEXT,
    PRIMARY KEY ((user_id), perform_time)
) WITH CLUSTERING ORDER BY (perform_time DESC)
   AND default_time_to_live = 2592000; 