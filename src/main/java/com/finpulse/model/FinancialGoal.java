package com.finpulse.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "financial_goals")
public class FinancialGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double targetAmount;
    private LocalDate targetDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Constructors, getters, and setters


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}