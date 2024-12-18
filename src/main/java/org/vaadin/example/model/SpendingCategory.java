package org.vaadin.example.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

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

    public void setUser(User currentUser) {
        this.user = currentUser;
    }
}


