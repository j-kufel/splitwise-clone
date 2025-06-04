package com.splitwise.config;

import com.splitwise.Role;
import com.splitwise.model.User;
import com.splitwise.service.UserService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

  @Autowired
  private UserService userService;

  @Override
  public void run(String... args) {
    String testEmail = "admin@example.com";

    Optional<User> optionalUser = userService.findByEmail(testEmail);

    User user;
    if (optionalUser.isPresent()) {
      user = optionalUser.get();
      System.out.println("Updating existing user: " + testEmail);
    } else {
      user = new User();
      user.setEmail(testEmail);
      System.out.println("Creating new user: " + testEmail);
    }

    user.setName("Test Admin");
    user.setPassword("password123");
    user.setRole(Role.ADMIN);

    User saved = userService.saveUser(user);
    System.out.println("User saved: " + saved.getId() + ", " + saved.getEmail());
  }
}
