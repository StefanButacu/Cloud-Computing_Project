package com.example.ccbe.service;

import com.example.ccbe.domain.CalorieValue;
import com.example.ccbe.domain.Food;
import com.example.ccbe.domain.dto.foodDTOS.FoodDetailsDTO;
import com.example.ccbe.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class FoodService {

    private final FoodRepository foodRepository;
    private static final Double HUNDRED = 100.0;

    @Autowired
    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public Food findFoodById(Long id) {
        return foodRepository.findById(id).orElse(null);
    }

    public Long calculateCaloriesForFood(Long foodId, Double quantity) {
        Food food = foodRepository.findById(foodId).orElse(null);
        if (food == null)
            return 0L;
        return (long) Math.floor(CalorieValue.PROTEIN.getCalorie() * food.getProtein() * quantity / HUNDRED +
                CalorieValue.CARBOHYDRATE.getCalorie() * food.getCarbohydrate() * quantity / HUNDRED +
                CalorieValue.LIPID.getCalorie() * food.getLipid() * quantity / HUNDRED);
    }

    public List<Food> getFoodsByName(String foodName) {
        return foodRepository.findAllByName(foodName);
    }

    public Page<Food> getFoodsByPage(Pageable pageable) {
        return foodRepository.findAll(pageable);
    }

    public Food save(FoodDetailsDTO toSaveFood) {
        Food food = new Food();
        food.setName(toSaveFood.getName());
        food.setProtein(toSaveFood.getProtein());
        food.setCarbohydrate(toSaveFood.getCarbohydrate());
        food.setLipid(toSaveFood.getLipid());
        return foodRepository.save(food);
    }
}
