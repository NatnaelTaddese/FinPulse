package com.finpulse.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "alipay_token")
    private String alipayToken;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;

    private double income;
    private double dailySpendingLimit;

    @Column(name = "monthly_saving_goal", nullable = true)
    private double monthlySavingGoal;

    @Column(name = "current_balance", nullable = true)
    private double currentBalance;
    @Column(name = "current_savings", nullable = true)
    private double currentSavings;

    private String paySchedule;
    private String firstName;
    private String lastName;
    private String preferredCurrency;


    private boolean onboardingCompleted;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FinancialGoal> financialGoals;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SpendingCategory> spendingCategories;

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public double getCurrentSavings() {
        return currentSavings;
    }

    public void setCurrentSavings(double currentSavings) {
        this.currentSavings = currentSavings;
    }

    public String getPaySchedule() {
        return paySchedule;
    }

    public void setPaySchedule(String paySchedule) {
        this.paySchedule = paySchedule;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPreferredCurrency() {
        return preferredCurrency;
    }

    public void setPreferredCurrency(String preferredCurrency) {
        this.preferredCurrency = preferredCurrency;
    }

    public double getMonthlySavingGoal() {
        return monthlySavingGoal;
    }

    public void setMonthlySavingGoal(double monthlySavingGoal) {
        this.monthlySavingGoal = monthlySavingGoal;
    }

    public User() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public double getDailySpendingLimit() {
        return dailySpendingLimit;
    }

    public boolean isOnboardingCompleted() {
        return onboardingCompleted;
    }

    public void setOnboardingCompleted(boolean onboardingCompleted) {
        this.onboardingCompleted = onboardingCompleted;
    }

    public Set<FinancialGoal> getFinancialGoals() {
        return financialGoals;
    }

    public Set<SpendingCategory> getSpendingCategories() {
        return spendingCategories;
    }

    public void setSpendingCategories(Set<SpendingCategory> spendingCategories) {
        this.spendingCategories = spendingCategories;
    }

    public void setDailySpendingLimit(double v) {
        this.dailySpendingLimit = v;
    }

    public String getAlipayToken() {
        return alipayToken;
    }

    public void setAlipayToken(String alipayToken) {
        this.alipayToken = alipayToken;
    }
}