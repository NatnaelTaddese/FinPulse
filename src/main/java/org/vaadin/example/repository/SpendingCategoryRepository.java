package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vaadin.example.model.SpendingCategory;
import org.vaadin.example.model.User;

import java.util.List;

@Repository
public interface SpendingCategoryRepository extends JpaRepository<SpendingCategory, Long> {
    List<SpendingCategory> findByUser(User user);
}