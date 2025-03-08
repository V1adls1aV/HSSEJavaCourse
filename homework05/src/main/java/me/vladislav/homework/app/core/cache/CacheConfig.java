package me.vladislav.homework.app.core.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
@RequiredArgsConstructor
public class CacheConfig {
  private final CacheProperties cacheProperties;

  @Bean
  public Caffeine<Object, Object> caffeineConfig(CacheProperties cacheProperties) {
    return Caffeine.newBuilder()
        .expireAfterWrite(cacheProperties.getExpireAfterSeconds(), TimeUnit.SECONDS)
        .initialCapacity(cacheProperties.getInitialCapacity());
  }

  @Bean
  public CaffeineCacheManager cacheManager(Caffeine<Object, Object> caffeine) {
    CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
    caffeineCacheManager.setCaffeine(caffeine);
    caffeineCacheManager.setAsyncCacheMode(true);
    return caffeineCacheManager;
  }
}
