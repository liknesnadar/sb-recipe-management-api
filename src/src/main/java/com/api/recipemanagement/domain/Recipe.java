package com.api.recipemanagement.domain;

import java.sql.Date;
import java.sql.Timestamp;

public class Recipe {

    private Integer recipeId;
    private Integer userId;
    private String recipeName;
    private String createdDateTime;
    private Boolean isVegetarian;
    private Integer numberOfPeople;
    private String ingredients;
    private String cookingInstructions;

    public Recipe(Integer recipeId, Integer userId, String recipeName, String createdDateTime, Boolean isVegetarian, Integer numberOfPeople, String ingredients, String cookingInstructions) {
        this.recipeId = recipeId;
        this.userId = userId;
        this.recipeName = recipeName;
        this.createdDateTime = createdDateTime;
        this.isVegetarian = isVegetarian;
        this.numberOfPeople = numberOfPeople;
        this.ingredients = ingredients;
        this.cookingInstructions = cookingInstructions;
    }

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Boolean getVegetarian() {
        return isVegetarian;
    }

    public void setVegetarian(Boolean vegetarian) {
        isVegetarian = vegetarian;
    }

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getCookingInstructions() {
        return cookingInstructions;
    }

    public void setCookingInstructions(String cookingInstructions) {
        this.cookingInstructions = cookingInstructions;
    }

}
