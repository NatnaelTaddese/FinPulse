package com.finpulse.repository;

import com.finpulse.model.FinancialGoal;
import com.finpulse.model.SpendingCategory;
import com.finpulse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    List<User> findByIncome(double income);
    List<User> findByDailySpendingLimit(double limit);
    List<User> findByFinancialGoals(FinancialGoal financialGoal);
    List<User> findBySpendingCategories(SpendingCategory category);
}