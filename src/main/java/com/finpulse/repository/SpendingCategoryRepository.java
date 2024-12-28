package com.finpulse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.finpulse.model.SpendingCategory;
import com.finpulse.model.User;

import java.util.List;

@Repository
public interface SpendingCategoryRepository extends JpaRepository<SpendingCategory, Long> {
    List<SpendingCategory> findByUser(User user);
}