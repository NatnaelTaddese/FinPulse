package org.vaadin.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.example.model.Expense;
import org.vaadin.example.model.User;
import org.vaadin.example.repository.ExpenseRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    // Save expense
    public void saveExpense(Expense expense) {
        expenseRepository.save(expense); // Save expense to database
    }

    // Get expenses for a user
    public List<Expense> getExpensesForUser(User user) {
        return expenseRepository.findByUser(user);
    }

    // Get expenses for a user filtered by category name
    public List<Expense> getExpensesByCategory(User user, String categoryName) {
        return expenseRepository.findByUserAndCategoryName(user, categoryName); // Adjusted to match the correct method
    }

    // Get expenses for a user within a date range (using createdAt)
    public List<Expense> getExpensesByDateRange(User user, LocalDateTime startDate, LocalDateTime endDate) {
        return expenseRepository.findByUserAndDateBetween(user, startDate, endDate);
    }
}