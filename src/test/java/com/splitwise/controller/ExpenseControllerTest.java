package com.splitwise.controller;

import com.splitwise.model.Expense;
import com.splitwise.model.Group;
import com.splitwise.model.User;
import com.splitwise.service.ExpenseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseControllerTest {
    
    @Mock
    private ExpenseService expenseService;
    
    @InjectMocks
    private ExpenseController expenseController;
    
    @Test
    void shouldCreateExpense() {
        Expense expense = new Expense();
        expense.setDescription("Test Expense");
        
        when(expenseService.createExpense(any(Expense.class))).thenReturn(expense);
        
        ResponseEntity<Expense> response = expenseController.createExpense(expense);
        
        verify(expenseService).createExpense(any(Expense.class));
        assert response.getStatusCode().is2xxSuccessful();
        assert response.getBody().getDescription().equals("Test Expense");
    }
    
    @Test
    void shouldGetExpense() {
        Expense expense = new Expense();
        expense.setId(1L);
        expense.setDescription("Test Expense");
        
        when(expenseService.getExpense(1L)).thenReturn(expense);
        
        ResponseEntity<Expense> response = expenseController.getExpense(1L);
        
        verify(expenseService).getExpense(1L);
        assert response.getStatusCode().is2xxSuccessful();
        assert response.getBody().getDescription().equals("Test Expense");
    }
    
    @Test
    void shouldDeleteExpense() {
        expenseController.deleteExpense(1L);
        verify(expenseService).deleteExpense(1L);
    }
    
    @Test
    void shouldGetGroupBalances() {
        Map<User, Double> balances = new HashMap<>();
        User user = new User();
        user.setId(1L);
        balances.put(user, 100.0);
        
        Group group = new Group();
        group.setId(1L);
        group.setName("Test Group");
        
        when(expenseService.getGroupById(1L)).thenReturn(group);
        when(expenseService.calculateBalancesForGroup(group)).thenReturn(balances);
        
        ResponseEntity<Map<User, Double>> response = expenseController.getGroupBalances(1L);
        
        verify(expenseService).getGroupById(1L);
        verify(expenseService).calculateBalancesForGroup(group);
        assert response.getStatusCode().is2xxSuccessful();
        assert response.getBody().get(user).equals(100.0);
    }
}
