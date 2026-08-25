package com.yummiee.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ShoppingListItemRequest {

    @NotBlank(message = "Item name is required")
    private String name;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Double quantity;

    @NotBlank(message = "Unit is required")
    private String unit;

    private Boolean checked;

    private Long recipeId;

    public ShoppingListItemRequest() {
    }

    public ShoppingListItemRequest(String name, Double quantity, String unit, Boolean checked, Long recipeId) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.checked = checked;
        this.recipeId = recipeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }
}
