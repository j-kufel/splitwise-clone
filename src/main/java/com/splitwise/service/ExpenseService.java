package com.splitwise.service;

import com.splitwise.model.*;
import com.splitwise.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ExpenseService {
    
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    public Expense createExpense(Expense expense) {
        return expenseRepository.save(expense);
    }
    
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }
    
    public Expense getExpense(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense with ID " + id + " not found"));
    }
    
    public User getUserById(Long id) {
        return userService.getUserById(id);
    }
    
    public Group getGroupById(Long id) {
        return groupService.getGroupById(id);
    }
    
    public Map<User, Double> calculateBalancesForGroup(Group group) {
        Map<User, Double> balances = new HashMap<>();
        
        for (User member : group.getMembers()) {
            balances.put(member, 0.0);
        }
        
        for (Expense expense : group.getExpenses()) {
            double payerShare = expense.getAmount() / expense.getShares().size();
            
            balances.put(expense.getPayer(), balances.get(expense.getPayer()) - payerShare);
            
            for (User participant : expense.getShares().keySet()) {
                balances.put(participant, balances.get(participant) + expense.getShares().get(participant));
            }
        }
        
        return balances;
    }
    
    public Map<User, Double> calculateBalancesBetweenUsers(User user1, User user2) {
        Map<User, Double> balances = new HashMap<>();
        balances.put(user1, 0.0);
        balances.put(user2, 0.0);
        
        List<Expense> expenses = expenseRepository.findByPayerOrSharesContains(user1, user2);
        
        for (Expense expense : expenses) {
            if (expense.getPayer().equals(user1)) {
                balances.put(user1, balances.get(user1) + expense.getAmount());
                for (Map.Entry<User, Double> entry : expense.getShares().entrySet()) {
                    if (entry.getKey().equals(user2)) {
                        balances.put(user2, balances.get(user2) - entry.getValue());
                    }
                }
            } else if (expense.getPayer().equals(user2)) {
                balances.put(user2, balances.get(user2) + expense.getAmount());
                for (Map.Entry<User, Double> entry : expense.getShares().entrySet()) {
                    if (entry.getKey().equals(user1)) {
                        balances.put(user1, balances.get(user1) - entry.getValue());
                    }
                }
            }
        }
        
        return balances;
    }
}
