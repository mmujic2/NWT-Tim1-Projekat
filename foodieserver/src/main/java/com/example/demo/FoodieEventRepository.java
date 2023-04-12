package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodieEventRepository extends JpaRepository<FoodieEvent, Integer> {
}
