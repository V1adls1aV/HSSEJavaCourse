package me.vladislav.homework.app.config;

import me.vladislav.homework.app.db.repository.user.UserRepository;
import me.vladislav.homework.app.dto.service.User;
import me.vladislav.homework.app.dto.service.UserData;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;

@TestConfiguration
public class TestMockitoConfig {
  private static final Logger log = LoggerFactory.getLogger(TestMockitoConfig.class);

  @Bean
  @Primary
  public UserRepository spyUserRepository() {
    UserRepository spyRepository = Mockito.spy(UserRepository.class);
    Set<Long> courseIds = new ConcurrentSkipListSet<>();

    // Mock create to always return ID 1
    Mockito.when(spyRepository.create(any(UserData.class))).thenReturn(1L);

    // Mock getById to return fixed user
    Mockito.when(spyRepository.getById(anyLong()))
        .thenReturn(new User(1L, "testuser", "test@example.com"));

    // Keep real logic only for course-related operations
    doAnswer(
        invocation -> {
          Long userId = invocation.getArgument(0);
          Long courseId = invocation.getArgument(1);
          if (userId.equals(1L)) {
            courseIds.add(courseId);
          }
          return null;
        })
        .when(spyRepository)
        .addCourseId(anyLong(), anyLong());

    Mockito.when(spyRepository.getCoursesIds(anyLong()))
        .thenAnswer(
            invocation -> {
              Long userId = invocation.getArgument(0);
              return userId.equals(1L) ? courseIds : Set.of();
            });

    // Mock other methods to return empty sets because, I will not use it in other places
    Mockito.when(spyRepository.getBooksIds(anyLong())).thenReturn(Set.of());
    Mockito.when(spyRepository.getUniversitiesIds(anyLong())).thenReturn(Set.of());

    return spyRepository;
  }
}
