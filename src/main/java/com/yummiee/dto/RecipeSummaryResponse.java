package com.yummiee.dto;

public class RecipeSummaryResponse {

    private Long id;
    private String name;
    private Integer time;
    private Integer servings;
    private String category;
    private String image;

    public RecipeSummaryResponse() {
    }

    public RecipeSummaryResponse(Long id, String name, Integer time, Integer servings, String category, String image) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.servings = servings;
        this.category = category;
        this.image = image;
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

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
