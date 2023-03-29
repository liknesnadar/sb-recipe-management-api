package com.api.recipemanagement.services;

import com.api.recipemanagement.domain.Recipe;
import com.api.recipemanagement.exceptions.BadRequestException;
import com.api.recipemanagement.exceptions.ResourceNotFoundException;

import java.sql.Date;
import java.util.List;

public interface RecipeService {

    List<Recipe> fetchAllRecipes(Integer userId) throws ResourceNotFoundException;

    Recipe fetchRecipeById(Integer userId, Integer recipeId) throws ResourceNotFoundException;

    Recipe addRecipe(Integer userId, String recipeName, Boolean isVegetarian, Integer numberOfPeople,
                     String ingredients, String cookingInstructions) throws BadRequestException;

    void updateRecipe(Integer userId, Integer recipeId, Recipe recipe) throws BadRequestException;

    void removeRecipe(Integer userId, Integer recipeId) throws ResourceNotFoundException;

}
