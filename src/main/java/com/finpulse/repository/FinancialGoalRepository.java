package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vaadin.example.model.FinancialGoal;
import org.vaadin.example.model.User;

import java.util.List;

@Repository
public interface FinancialGoalRepository extends JpaRepository<FinancialGoal, Long> {
    List<FinancialGoal> findByUser(User user);
}