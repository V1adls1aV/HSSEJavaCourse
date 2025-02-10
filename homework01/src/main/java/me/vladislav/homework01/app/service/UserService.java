package me.vladislav.homework01.app.service;

import lombok.extern.slf4j.Slf4j;
import me.vladislav.homework01.app.db.repository.user.UserRepository;
import me.vladislav.homework01.app.dto.api.request.UserCreateRequest;
import me.vladislav.homework01.app.dto.service.User;
import me.vladislav.homework01.app.dto.service.UserData;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Long createUser(UserCreateRequest request) {
    log.info("Creating new user with username: {}", request.username());
    Long userId = userRepository.create(new UserData(request.username(), request.email()));
    log.info("Created new user with id: {}", userId);
    return userId;
  }

  public User getUserById(Long userId) {
    log.info("Retrieving user with id: {}", userId);
    User user = userRepository.getById(userId);
    log.info("Found user: {}", user.username());
    return user;
  }
}
