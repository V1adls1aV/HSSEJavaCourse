package me.vladislav.homework01.app.service;

import me.vladislav.homework01.app.db.repository.user.UserRepository;
import me.vladislav.homework01.app.dto.api.request.UserCreateRequest;
import me.vladislav.homework01.app.dto.service.User;
import me.vladislav.homework01.app.dto.service.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Long createUser(UserCreateRequest request) {
    return userRepository.create(new UserData(request.username(), request.email()));
  }

  public User getUserById(Long userId) {
    return userRepository.getById(userId);
  }
}
