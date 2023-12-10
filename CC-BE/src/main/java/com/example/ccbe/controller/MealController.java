package com.example.ccbe.controller;

import com.example.ccbe.domain.Meal;
import com.example.ccbe.domain.dto.foodDTOS.MealDTO;
import com.example.ccbe.service.MealService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/meal")
public class MealController {

    private final MealService mealService;
    private final ModelMapper modelMapper;

    public MealController(MealService mealService, ModelMapper modelMapper) {
        this.mealService = mealService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{meal_id}")
    public ResponseEntity<?> findMealById(@PathVariable("meal_id") Long mealId) {
        Meal meal = mealService.findMealById(mealId);
        if (meal == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(modelMapper.map(meal, MealDTO.class), HttpStatus.OK);
    }
}
