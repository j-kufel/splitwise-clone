package com.splitwise.controller;

import com.splitwise.model.*;
import com.splitwise.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    
    @Autowired
    private ExpenseService expenseService;
    
    @PostMapping
    public ResponseEntity<Expense> createExpense(@RequestBody Expense expense) {
        return ResponseEntity.ok(expenseService.createExpense(expense));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpense(@PathVariable Long id) {
        return ResponseEntity.ok(expenseService.getExpense(id));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/group/{groupId}/balances")
    public ResponseEntity<Map<User, Double>> getGroupBalances(@PathVariable Long groupId) {
        Group group = expenseService.getGroupById(groupId);
        return ResponseEntity.ok(expenseService.calculateBalancesForGroup(group));
    }
}
