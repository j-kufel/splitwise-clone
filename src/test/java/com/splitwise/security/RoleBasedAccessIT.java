package com.splitwise.security;

import com.splitwise.controller.GroupController;
import com.splitwise.controller.ExpenseController;
import com.splitwise.model.Expense;
import com.splitwise.model.Group;
import com.splitwise.model.User;
import com.splitwise.service.ExpenseService;
import com.splitwise.service.GroupService;
import com.splitwise.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = { GroupController.class, ExpenseController.class })
@Import(SecurityConfig.class)
public class RoleBasedAccessIT {

  @MockBean
  private UserService userService;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private GroupService groupService;

  @MockBean
  private ExpenseService expenseService;

  @Test
  @WithMockUser(username = "user@example.com", roles = "USER")
  void normalUserCanDeleteOwnGroup() throws Exception {
    User user = new User();
    user.setEmail("user@example.com");
    Group group = new Group();
    group.setId(1L);
    group.setCreatedBy(user);

    when(userService.findByEmail("user@example.com")).thenReturn(user);
    when(groupService.getGroupById(1L)).thenReturn(group);
    doNothing().when(groupService).deleteGroup(1L);

    mockMvc.perform(delete("/api/groups/1"))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user@example.com", roles = "USER")
  void normalUserCannotDeleteOtherUsersGroup() throws Exception {
    User user = new User();
    user.setEmail("user@example.com");

    User otherUser = new User();
    otherUser.setEmail("other@example.com");
    Group group = new Group();
    group.setId(1L);
    group.setCreatedBy(otherUser);

    when(userService.findByEmail("user@example.com")).thenReturn(user);
    when(groupService.getGroupById(1L)).thenReturn(group);

    mockMvc.perform(delete("/api/groups/1"))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(username = "admin@example.com", roles = "ADMIN")
  void adminCanDeleteAnyGroup() throws Exception {
    User admin = new User();
    admin.setEmail("admin@example.com");
    Group group = new Group();
    group.setId(1L);

    when(userService.findByEmail("admin@example.com")).thenReturn(admin);
    when(groupService.getGroupById(1L)).thenReturn(group);
    doNothing().when(groupService).deleteGroup(1L);

    mockMvc.perform(delete("/api/groups/1"))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user@example.com", roles = "USER")
  void normalUserCanDeleteOwnExpense() throws Exception {
    User user = new User();
    user.setEmail("user@example.com");
    Expense expense = new Expense();
    expense.setId(1L);
    expense.setCreatedBy(user);

    when(userService.findByEmail("user@example.com")).thenReturn(user);
    when(expenseService.getExpense(1L)).thenReturn(expense);
    doNothing().when(expenseService).deleteExpense(1L);

    mockMvc.perform(delete("/api/expenses/1"))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user@example.com", roles = "USER")
  void normalUserCannotDeleteOtherUsersExpense() throws Exception {
    User user = new User();
    user.setEmail("user@example.com");

    User otherUser = new User();
    otherUser.setEmail("other@example.com");
    Expense expense = new Expense();
    expense.setId(1L);
    expense.setCreatedBy(otherUser);

    when(userService.findByEmail("user@example.com")).thenReturn(user);
    when(expenseService.getExpense(1L)).thenReturn(expense);

    mockMvc.perform(delete("/api/expenses/1"))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(username = "admin@example.com", roles = "ADMIN")
  void adminCanDeleteAnyExpense() throws Exception {
    User admin = new User();
    admin.setEmail("admin@example.com");
    Expense expense = new Expense();
    expense.setId(1L);

    when(userService.findByEmail("admin@example.com")).thenReturn(admin);
    when(expenseService.getExpense(1L)).thenReturn(expense);
    doNothing().when(expenseService).deleteExpense(1L);

    mockMvc.perform(delete("/api/expenses/1"))
        .andExpect(status().isOk());
  }
}
