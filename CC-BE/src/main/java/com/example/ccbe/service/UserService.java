package com.example.ccbe.service;

import com.example.ccbe.domain.CalorieValue;
import com.example.ccbe.domain.dto.DietPlanDTO;
import com.example.ccbe.domain.dto.userDTOS.UserFitnessRequestDTO;
import com.example.ccbe.domain.dto.userDTOS.UserRegisterRequestDTO;
import com.example.ccbe.domain.user.ActivityLevel;
import com.example.ccbe.domain.user.Gender;
import com.example.ccbe.domain.user.User;
import com.example.ccbe.repository.UserRepository;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final DietPlanDTO DEFAULT_DIET_PLAN = new DietPlanDTO(2000.0, 137.0, 137.0, 100.0);

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User loadUserByUsername(String username) {
        return userRepository.findUserByUsername(username).orElse(null);
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public DietPlanDTO requestUserDietPlan(UserFitnessRequestDTO requestDTO) {
        double calorieGoal = Math.floor(calculateCalorieGoal(requestDTO));
        switch (requestDTO.getDietType()) {
            case BALANCED -> {
                return new DietPlanDTO(calorieGoal, calorieGoal * 0.3 / CalorieValue.PROTEIN.getCalorie(), calorieGoal * 0.4 / CalorieValue.CARBOHYDRATE.getCalorie(), calorieGoal * 0.3 / CalorieValue.LIPID.getCalorie());
            }
            case LOW_FAT -> {
                return new DietPlanDTO(calorieGoal, calorieGoal * 0.3 / CalorieValue.PROTEIN.getCalorie(), calorieGoal * 0.5 / CalorieValue.CARBOHYDRATE.getCalorie(), calorieGoal * 0.2 / CalorieValue.LIPID.getCalorie());
            }
            case LOW_CARBS -> {
                return new DietPlanDTO(calorieGoal, calorieGoal * 0.35 / CalorieValue.PROTEIN.getCalorie(), calorieGoal * 0.3 / CalorieValue.CARBOHYDRATE.getCalorie(), calorieGoal * 0.35 / CalorieValue.LIPID.getCalorie());
            }
            case HIGH_PROTEIN -> {
                return new DietPlanDTO(calorieGoal, calorieGoal * 0.4 / CalorieValue.PROTEIN.getCalorie(), calorieGoal * 0.3 / CalorieValue.CARBOHYDRATE.getCalorie(), calorieGoal * 0.3 / CalorieValue.LIPID.getCalorie());
            }
            default -> {
                return DEFAULT_DIET_PLAN;
            }
        }
    }

    public User registerUser(UserRegisterRequestDTO userRegisterRequestDTO) {
        if (loadUserByUsername(userRegisterRequestDTO.getUsername()) != null)
            throw new RuntimeException("Invalid username");
        DietPlanDTO dietPlan = requestUserDietPlan(userRegisterRequestDTO.getUserFitnessRequest());
        User user = createUser(userRegisterRequestDTO, userRegisterRequestDTO.getUserFitnessRequest(), dietPlan);
        user = userRepository.save(user);
        return user;
    }

    private User createUser(UserRegisterRequestDTO userRegisterRequestDTO, UserFitnessRequestDTO userFitnessRequestDTO, DietPlanDTO dietPlan) {
        User user = new User();
        user.setUsername(userRegisterRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userRegisterRequestDTO.getPassword()));
        user.setStartWeight(userRegisterRequestDTO.getStartWeight());
        user.setCurrentWeight(userFitnessRequestDTO.getWeight());
        user.setGoalWeight(userRegisterRequestDTO.getGoalWeight());
        user.setHeight(userFitnessRequestDTO.getHeight());
        user.setDietType(userFitnessRequestDTO.getDietType());
        ///
        user.setProteinGoal(dietPlan.getProtein());
        user.setCarbohydrateGoal(dietPlan.getCarbs());
        user.setLipidGoal(dietPlan.getLipid());
        ///
        user.setActivityLevel(userFitnessRequestDTO.getActivityLevel());
        user.setGender(userFitnessRequestDTO.getGender());
        user.setWeightGoal(userFitnessRequestDTO.getWeightGoal());
        //
        return user;
    }

    public void updateCurrentWeight(Long userId, Double currentWeight) {
        User user = findById(userId);
        if (user == null) {
            throw new RuntimeException("Invalid user");
        }
        user.setCurrentWeight(currentWeight);
        userRepository.save(user);
    }

    private double calculateBMR(double weight, double height, int age, Gender gender, ActivityLevel activityLevel) {
        if (gender == Gender.MALE) {
            return (10 * weight + 6.25 * height - 5 * age + 5) * activityLevel.getActivityFactor();
        } else {
            return (10 * weight + 6.25 * height - 5 * age - 161) * activityLevel.getActivityFactor();
        }
    }

    private double calculateCalorieGoal(UserFitnessRequestDTO requestDTO) {
        double bmr = calculateBMR(requestDTO.getWeight(), requestDTO.getHeight(), requestDTO.getAge(), requestDTO.getGender(), requestDTO.getActivityLevel());
        switch (requestDTO.getWeightGoal()) {
            case MAINTAIN -> {
                return bmr;
            }
            case WEIGHT_GAIN -> {
                return 1.15 * bmr;
            }
            case WEIGHT_LOSE -> {
                return 0.85 * bmr;
            }
            default -> {
                return bmr;
            }
        }
    }
}
