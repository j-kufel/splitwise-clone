package com.splitwise.controller;

import com.splitwise.model.User;
import com.splitwise.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

  @Mock
  private UserService userService;

  @InjectMocks
  private AuthController authController;

  private User user;

  @BeforeEach
  void setup() {
    user = new User();
    user.setEmail("test@example.com");
    user.setPassword("password");
  }

  @Test
  void shouldLoginSuccessfully() {
    when(userService.authenticate("test@example.com", "password")).thenReturn(user);

    ResponseEntity<User> response = authController.login(user);

    assertEquals(200, response.getStatusCode().value());
    assertEquals(user, response.getBody());
  }

  @Test
  void shouldReturn401OnInvalidLogin() {
    when(userService.authenticate("test@example.com", "password"))
        .thenThrow(new RuntimeException("Invalid credentials"));

    ResponseEntity<User> response = authController.login(user);

    assertEquals(401, response.getStatusCode().value());
    assertNull(response.getBody());
  }
}
