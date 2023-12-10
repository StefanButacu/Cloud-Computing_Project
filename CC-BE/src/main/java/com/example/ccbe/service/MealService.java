package com.example.ccbe.service;

import com.example.ccbe.domain.Meal;
import com.example.ccbe.repository.MealRepository;
import org.springframework.stereotype.Service;

@Service
public class MealService {

    private final MealRepository mealRepository;

    public MealService(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    public Meal findMealById(Long mealId) {
        return mealRepository.findById(mealId).orElse(null);
    }
}
