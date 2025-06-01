package com.splitwise.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private Double amount;
    
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
    
    @ManyToOne
    @JoinColumn(name = "payer_id", nullable = false)
    private User payer;
    
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;
    
    @ElementCollection
    @CollectionTable(
        name = "expense_shares",
        joinColumns = @JoinColumn(name = "expense_id")
    )
    @MapKeyJoinColumn(name = "user_id")
    @Column(name = "share_amount")
    private Map<User, Double> shares;
}
