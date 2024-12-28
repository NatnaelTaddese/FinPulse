package com.finpulse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.finpulse.model.FinancialGoal;
import com.finpulse.model.SpendingCategory;
import com.finpulse.model.User;
import com.finpulse.repository.FinancialGoalRepository;
import com.finpulse.repository.SpendingCategoryRepository;
import com.finpulse.repository.UserRepository;

@Service
public class UserService {
    @Autowired
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

    public void saveUser(User user) {
        userRepository.save(user);
    }
}