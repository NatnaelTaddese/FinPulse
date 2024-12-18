package org.vaadin.example.service;

import org.springframework.stereotype.Service;
import org.vaadin.example.model.FinancialGoal;
import org.vaadin.example.model.SpendingCategory;
import org.vaadin.example.repository.FinancialGoalRepository;
import org.vaadin.example.repository.SpendingCategoryRepository;
import org.vaadin.example.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final FinancialGoalRepository financialGoalRepository;
    private final SpendingCategoryRepository spendingCategoryRepository;

    public UserService(
            UserRepository userRepository,
            FinancialGoalRepository financialGoalRepository,
            SpendingCategoryRepository spendingCategoryRepository
    ) {
        this.userRepository = userRepository;
        this.financialGoalRepository = financialGoalRepository;
        this.spendingCategoryRepository = spendingCategoryRepository;
    }

    public void saveFinancialGoal(FinancialGoal goal) {
        financialGoalRepository.save(goal);
    }

    public void updateFinancialGoal(FinancialGoal goal) {
        financialGoalRepository.save(goal);
    }

    public void deleteFinancialGoal(FinancialGoal goal) {
        financialGoalRepository.delete(goal);
    }

    public void addSpendingCategory(SpendingCategory category) {
        spendingCategoryRepository.save(category);
    }

    public void updateSpendingCategory(SpendingCategory category) {
        spendingCategoryRepository.save(category);
    }

    public void deleteSpendingCategory(SpendingCategory category) {
        spendingCategoryRepository.delete(category);
    }
}