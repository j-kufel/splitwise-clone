package com.splitwise.security;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Optional;

import com.splitwise.model.Expense;
import com.splitwise.model.Group;
import com.splitwise.model.User;
import com.splitwise.service.ExpenseService;
import com.splitwise.service.GroupService;
import com.splitwise.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
  public PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            // ✅ Permit Swagger UI and OpenAPI access
            .requestMatchers(
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/swagger-resources/**",
                "/webjars/**")
            .permitAll()
            .requestMatchers("/api/groups/**", "/api/expenses/**").hasAnyRole("USER", "ADMIN")

            .requestMatchers(HttpMethod.DELETE, "/api/groups/**").access((authenticationSupplier, context) -> {
              HttpServletRequest request = context.getRequest();
              String email = authenticationSupplier.get().getName();
              Optional<User> currentUser = userService.findByEmail(email);

              if (currentUser.isEmpty())
                return new AuthorizationDecision(false);

              boolean isAdmin = authenticationSupplier.get().getAuthorities().stream()
                  .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

              try {
                Long id = Long.parseLong(request.getRequestURI().replaceAll(".*/", ""));
                Group group = groupService.getGroupById(id);
                return new AuthorizationDecision(
                    isAdmin || group.getCreatedBy().getId().equals(currentUser.get().getId()));
              } catch (Exception e) {
                return new AuthorizationDecision(false);
              }
            })

            .requestMatchers(HttpMethod.DELETE, "/api/expenses/**").access((authenticationSupplier, context) -> {
              HttpServletRequest request = context.getRequest();
              String email = authenticationSupplier.get().getName();
              Optional<User> currentUser = userService.findByEmail(email);

              if (currentUser.isEmpty())
                return new AuthorizationDecision(false);

              boolean isAdmin = authenticationSupplier.get().getAuthorities().stream()
                  .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

              try {
                Long id = Long.parseLong(request.getRequestURI().replaceAll(".*/", ""));
                Expense expense = expenseService.getExpense(id);
                return new AuthorizationDecision(
                    isAdmin || expense.getCreatedBy().getId().equals(currentUser.get().getId()));
              } catch (Exception e) {
                return new AuthorizationDecision(false);
              }
            })

            .anyRequest().authenticated())
        .httpBasic(withDefaults());

    return http.build();
  }
}
