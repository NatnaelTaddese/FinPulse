package com.finpulse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.finpulse.model.Expense;
import com.finpulse.model.SpendingCategory;
import com.finpulse.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUser(User user);

    List<Expense> findByUserAndCategoryName(User user, String categoryName);

//    List<Expense> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);

    List<Expense> findByUserAndDateBetween(User user, LocalDateTime startDate, LocalDateTime endDate);

    List<Expense> findByCategory(SpendingCategory category);
}
