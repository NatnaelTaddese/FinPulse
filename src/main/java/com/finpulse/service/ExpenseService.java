package com.finpulse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.finpulse.model.Expense;
import com.finpulse.model.SpendingCategory;
import com.finpulse.model.User;
import com.finpulse.repository.ExpenseRepository;
import com.finpulse.repository.SpendingCategoryRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final SpendingCategoryRepository spendingCategoryRepository;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository, SpendingCategoryRepository spendingCategoryRepository) {
        this.expenseRepository = expenseRepository;
        this.spendingCategoryRepository = spendingCategoryRepository;
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
        return expenseRepository.findByUserAndCategoryName(user, categoryName);
    }

    // Get expenses for a user within a date range (using createdAt)
    public List<Expense> getExpensesByDateRange(User user, LocalDateTime startDate, LocalDateTime endDate) {
        return expenseRepository.findByUserAndDateBetween(user, startDate, endDate);
    }

    // Delete spending category and update expenses
    public void deleteSpendingCategory(SpendingCategory category) {
        List<Expense> expenses = expenseRepository.findByCategory(category);
        for (Expense expense : expenses) {
            expense.setCategory(null); // Remove reference to the category
            expenseRepository.save(expense);
        }
        spendingCategoryRepository.delete(category);
    }

    public List<Expense> getExpensesByUser(User currentUser) {
        return expenseRepository.findByUser(currentUser);
    }

    public double getTodaySpending(User currentUser) {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        List<Expense> todayExpenses = expenseRepository.findByUserAndDateBetween(currentUser, startOfDay, endOfDay);
        return todayExpenses.stream().mapToDouble(Expense::getAmount).sum();
    }
}