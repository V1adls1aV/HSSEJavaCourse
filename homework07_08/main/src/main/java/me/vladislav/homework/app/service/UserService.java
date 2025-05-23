package me.vladislav.homework.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.homework.app.core.exception.db.repository.UserNotFoundException;
import me.vladislav.homework.app.db.orm.User;
import me.vladislav.homework.app.db.repository.UserRepository;
import me.vladislav.homework.app.dto.api.request.UserCreateRequest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  @Async("taskExecutor")
  @Transactional
  public CompletableFuture<Long> createUser(UserCreateRequest request) {
    log.info("Creating new user with username: {}", request.username());
    long userId = userRepository.save(new User(null, request.username(), request.email())).getId();
    log.info("Created new user with id: {}", userId);
    return CompletableFuture.completedFuture(userId);
  }

  @Async("taskExecutor")
  @Cacheable(value = "users", key = "#userId")
  @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
  public CompletableFuture<User> getUserById(Long userId) {
    log.info("Retrieving user with id: {}", userId);
    User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    log.info("Found user: {}", user.getUsername());
    return CompletableFuture.completedFuture(user);
  }
}
