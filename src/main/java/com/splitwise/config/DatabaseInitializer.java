package com.splitwise.config;

import com.splitwise.Role;
import com.splitwise.model.User;
import com.splitwise.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    
    @Autowired
    private UserService userService;
    
    @Override
    public void run(String... args) throws Exception {
        // Create a test user
        User user = new User();
        user.setEmail("test@example.com");
        user.setName("Test User");
        user.setPassword("password123");
        user.setRole(Role.USER);
        userService.createUser(user);
        
        System.out.println("Test user created successfully!");
    }
}
