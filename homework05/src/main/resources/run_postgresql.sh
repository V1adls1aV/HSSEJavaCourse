#!/usr/bin/env bash
docker run --name "JavaMTSlon" \
  -e POSTGRES_USER=CleverOne \
  -e POSTGRES_PASSWORD=compl3xNstrong \
  -e POSTGRES_DB=homework \
  -p 5432:5432 \
  -d "postgres:16-alpine"
