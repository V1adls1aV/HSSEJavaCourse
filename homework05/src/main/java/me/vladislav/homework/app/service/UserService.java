package me.vladislav.homework.app.service;

import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.homework.app.db.repository.UserRepository;
import me.vladislav.homework.app.dto.api.request.UserCreateRequest;
import me.vladislav.homework.app.db.orm.User;
import me.vladislav.homework.app.dto.service.UserData;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  @Async("taskExecutor")
  public CompletableFuture<Long> createUser(UserCreateRequest request) {
    log.info("Creating new user with username: {}", request.username());
    Long userId = userRepository.create(new UserData(request.username(), request.email()));
    log.info("Created new user with id: {}", userId);
    return CompletableFuture.completedFuture(userId);
  }

  @Async("taskExecutor")
  @Cacheable(value = "users", key = "#userId")
  public CompletableFuture<User> getUserById(Long userId) {
    log.info("Retrieving user with id: {}", userId);
    User user = userRepository.getById(userId);
    log.info("Found user: {}", user.username());
    return CompletableFuture.completedFuture(user);
  }
}
