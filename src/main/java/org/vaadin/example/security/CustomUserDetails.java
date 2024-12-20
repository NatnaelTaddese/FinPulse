package org.vaadin.example.security;

import org.springframework.security.core.GrantedAuthority;
import org.vaadin.example.model.FinancialGoal;
import org.vaadin.example.model.SpendingCategory;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User implements Serializable {
    private static final long serialVersionUID = 1L;

    private Double income;
    private Double dailySpendingLimit;
    private Set<FinancialGoal> financialGoals;
    private Set<SpendingCategory> spendingCategories;

    public CustomUserDetails(
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            Double income,
            Double dailySpendingLimit,
            Set<FinancialGoal> financialGoals,
            Set<SpendingCategory> spendingCategories
    ) {
        super(username, password, authorities);
        this.income = income;
        this.dailySpendingLimit = dailySpendingLimit;
        this.financialGoals = financialGoals;
        this.spendingCategories = spendingCategories;
    }

    // Add getters for the additional fields
    public Double getIncome() {
        return income;
    }

    public Double getDailySpendingLimit() {
        return dailySpendingLimit;
    }

    public Set<FinancialGoal> getFinancialGoals() {
        return financialGoals;
    }

    public Set<SpendingCategory> getSpendingCategories() {
        return spendingCategories;
    }

    // setters
    public void setIncome(Double income) {
        this.income = income;
    }

    public void setDailySpendingLimit(Double dailySpendingLimit) {
        this.dailySpendingLimit = dailySpendingLimit;
    }

    public void setFinancialGoals(Set<FinancialGoal> financialGoals) {
        this.financialGoals = financialGoals;
    }

    public void setSpendingCategories(Set<SpendingCategory> spendingCategories) {
        this.spendingCategories = spendingCategories;
    }
}