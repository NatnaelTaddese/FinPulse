package com.finpulse.model;

import jakarta.persistence.*;

@Entity
@Table(name = "spending_categories")
public class SpendingCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double budgetAmount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String categoryName) {
        this.name = categoryName;
    }

    public String getName() {
        return name;
    }

    public double getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(double budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public void setUser(User currentUser) {
        this.user = currentUser;
    }
}


