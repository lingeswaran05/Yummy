package com.yummiee.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class InstructionRequest {

    @NotNull(message = "Step number is required")
    @Positive(message = "Step number must be positive")
    private Integer step;

    private String title;

    @NotBlank(message = "Step description is required")
    private String description;

    public InstructionRequest() {
    }

    public InstructionRequest(Integer step, String title, String description) {
        this.step = step;
        this.title = title;
        this.description = description;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
