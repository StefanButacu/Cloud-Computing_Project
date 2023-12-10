package com.example.ccbe.domain.dto;

public class DietPlanDTO {

    Double calorie;
    Double protein;
    Double carbs;
    Double lipid;

    public DietPlanDTO(Double calorie, Double protein, Double carbs, Double lipid) {
        this.calorie = Math.floor(calorie);
        this.protein = Math.floor(protein);
        this.carbs = Math.floor(carbs);
        this.lipid = Math.floor(lipid);
    }

    public Double getCalorie() {
        return calorie;
    }

    public void setCalorie(Double calorie) {
        this.calorie = calorie;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public Double getCarbs() {
        return carbs;
    }

    public void setCarbs(Double carbs) {
        this.carbs = carbs;
    }

    public Double getLipid() {
        return lipid;
    }

    public void setLipid(Double lipid) {
        this.lipid = lipid;
    }
}
