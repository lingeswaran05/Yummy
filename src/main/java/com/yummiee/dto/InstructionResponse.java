package com.yummiee.dto;

public class InstructionResponse {

    private Long id;
    private Integer step;
    private String title;
    private String description;

    public InstructionResponse() {
    }

    public InstructionResponse(Long id, Integer step, String title, String description) {
        this.id = id;
        this.step = step;
        this.title = title;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
