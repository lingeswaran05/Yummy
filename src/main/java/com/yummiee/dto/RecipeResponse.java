package com.yummiee.dto;

import java.util.ArrayList;
import java.util.List;

public class RecipeResponse {

    private Long id;
    private String name;
    private String description;
    private String image;
    private String category;
    private Integer time;
    private String difficulty;
    private Integer servings;
    private Double rating;
    private Integer reviews;
    private List<IngredientResponse> ingredients = new ArrayList<>();
    private List<InstructionResponse> instructions = new ArrayList<>();
    private NutritionResponse nutrition;
    private String notes;

    public RecipeResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getReviews() {
        return reviews;
    }

    public void setReviews(Integer reviews) {
        this.reviews = reviews;
    }

    public List<IngredientResponse> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientResponse> ingredients) {
        this.ingredients = ingredients;
    }

    public List<InstructionResponse> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<InstructionResponse> instructions) {
        this.instructions = instructions;
    }

    public NutritionResponse getNutrition() {
        return nutrition;
    }

    public void setNutrition(NutritionResponse nutrition) {
        this.nutrition = nutrition;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
