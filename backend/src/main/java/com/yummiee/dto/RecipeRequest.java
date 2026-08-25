package com.yummiee.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

public class RecipeRequest {

    @NotBlank(message = "Recipe name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Time is required")
    @Positive(message = "Time must be positive")
    private Integer time;

    @NotBlank(message = "Difficulty is required")
    @Pattern(regexp = "Easy|Medium|Hard", message = "Difficulty must be Easy, Medium, or Hard")
    private String difficulty;

    @NotNull(message = "Servings is required")
    @Positive(message = "Servings must be positive")
    private Integer servings;

    private String image;

    @Valid
    private List<IngredientRequest> ingredients = new ArrayList<>();

    @Valid
    private List<InstructionRequest> instructions = new ArrayList<>();

    @Valid
    private NutritionRequest nutrition;

    private String notes;

    public RecipeRequest() {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<IngredientRequest> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientRequest> ingredients) {
        this.ingredients = ingredients;
    }

    public List<InstructionRequest> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<InstructionRequest> instructions) {
        this.instructions = instructions;
    }

    public NutritionRequest getNutrition() {
        return nutrition;
    }

    public void setNutrition(NutritionRequest nutrition) {
        this.nutrition = nutrition;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
