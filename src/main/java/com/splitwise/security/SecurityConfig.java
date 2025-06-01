package com.splitwise.security;

import static org.springframework.security.config.Customizer.withDefaults;

import com.splitwise.model.Expense;
import com.splitwise.model.Group;
import com.splitwise.model.User;
import com.splitwise.service.ExpenseService;
import com.splitwise.service.GroupService;
import com.splitwise.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  private GroupService groupService;

  @Autowired
  private ExpenseService expenseService;

  @Autowired
  private UserService userService;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/groups/**", "/api/expenses/**")
            .access((authentication, context) -> {
              HttpServletRequest request = context.getRequest();
              String uri = request.getRequestURI();

              String email = authentication.get().getName();

              User currentUser = userService.findByEmail(email);
              if (currentUser == null) {
                return new AuthorizationDecision(false);
              }

              boolean isAdmin = authentication.get().getAuthorities().stream()
                  .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

              String[] parts = uri.split("/");
              Long id;
              try {
                id = Long.parseLong(parts[parts.length - 1]);
              } catch (Exception e) {
                return new AuthorizationDecision(false);
              }

              try {
                if (uri.contains("/api/groups")) {
                  Group group = groupService.getGroupById(id);
                  return new AuthorizationDecision(isAdmin || group.getCreatedBy().equals(currentUser));
                } else if (uri.contains("/api/expenses")) {
                  Expense expense = expenseService.getExpense(id);
                  return new AuthorizationDecision(isAdmin || expense.getCreatedBy().equals(currentUser));
                }
              } catch (Exception e) {
                return new AuthorizationDecision(false);
              }

              return new AuthorizationDecision(false);
            }))
        .httpBasic(withDefaults());

    return http.build();
  }
}
