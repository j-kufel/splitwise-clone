package com.splitwise;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

  @Test
  void shouldContainUserAndAdminRoles() {
    assertNotNull(Role.valueOf("USER"));
    assertNotNull(Role.valueOf("ADMIN"));
  }

  @Test
  void shouldListAllRoles() {
    Role[] roles = Role.values();
    assertTrue(roles.length >= 2);
  }
}
