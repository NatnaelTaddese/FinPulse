package org.vaadin.example.repository;

import org.vaadin.example.model.FinancialGoal;
import org.vaadin.example.model.SpendingCategory;
import org.vaadin.example.model.User;
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