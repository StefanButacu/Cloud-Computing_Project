package com.example.ccbe.repository;

import com.example.ccbe.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {

    @Query(value = "select f from Food f " +
            "left join FoodMeal fm on fm.foodMealId.foodId = f.id " +
            "left join Meal m on fm.foodMealId.mealId = m.id " +
            "where m.id = :mealId ")
    List<Food> getFoodsForMeal(@Param("mealId") Long mealId);

    @Query("SELECT f FROM Food f WHERE lower(f.name) LIKE lower(concat('%', :foodName, '%'))")
    List<Food> findAllByName(@Param("foodName") String foodName);
}
