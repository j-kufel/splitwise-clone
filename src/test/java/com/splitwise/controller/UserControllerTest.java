package com.splitwise.controller;

import com.splitwise.model.User;
import com.splitwise.Role;
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
class UserControllerTest {

  @Mock
  private UserService userService;

  @InjectMocks
  private UserController userController;

  private User user;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setId(1L);
    user.setEmail("test@example.com");
    user.setPassword("secret");
    user.setName("Test User");
    user.setRole(Role.USER);
  }

  @Test
  void shouldCreateUser() {
    when(userService.createUser(any(User.class))).thenReturn(user);

    ResponseEntity<User> response = userController.createUser(user);

    assertEquals(200, response.getStatusCode().value());
    assertEquals(user, response.getBody());
    verify(userService).createUser(any(User.class));
  }

  @Test
  void shouldGetUserById() {
    when(userService.getUserById(1L)).thenReturn(user);

    ResponseEntity<User> response = userController.getUser(1L);

    assertEquals(200, response.getStatusCode().value());
    assertEquals(user, response.getBody());
  }

  @Test
  void shouldGetUserByEmail() {
    when(userService.getUserByEmail("test@example.com")).thenReturn(user);

    ResponseEntity<User> response = userController.getUserByEmail("test@example.com");

    assertEquals(200, response.getStatusCode().value());
    assertEquals(user, response.getBody());
  }

  @Test
  void shouldDeleteUser() {
    doNothing().when(userService).deleteUser(1L);

    ResponseEntity<Void> response = userController.deleteUser(1L);

    assertEquals(200, response.getStatusCode().value());
    verify(userService).deleteUser(1L);
  }
}
