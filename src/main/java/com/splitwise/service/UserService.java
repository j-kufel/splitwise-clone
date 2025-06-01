package com.splitwise.service;

import com.splitwise.Role;
import com.splitwise.model.User;
import com.splitwise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  public User createUser(User user) {
    if (user.getRole() == null) {
      user.setRole(Role.USER);
    }
    return userRepository.save(user);
  }

  public User getUserById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found"));
  }

  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User with email " + email + " not found"));
  }

  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }

  public User authenticate(String email, String password) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User with email " + email + " not found"));

    if (!user.getPassword().equals(password)) {
      throw new RuntimeException("Invalid password");
    }

    return user;
  }

  public User findByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
  }

}
