package com.api.recipemanagement.repositories;

import com.api.recipemanagement.domain.Recipe;
import com.api.recipemanagement.exceptions.BadRequestException;
import com.api.recipemanagement.exceptions.ResourceNotFoundException;

import java.sql.Date;
import java.util.List;

public interface RecipeRepository {

    List<Recipe> findAll(Integer userId) throws ResourceNotFoundException;

    Recipe findById(Integer userId, Integer recipeId) throws ResourceNotFoundException;

    Integer create(Integer userId, String recipeName, Boolean isVegetarian, Integer numberOfPeople,
                   String ingredients, String cookingInstructions) throws BadRequestException;

    void update(Integer userId, Integer recipeId, Recipe recipe) throws BadRequestException;

    void removeById(Integer userId, Integer recipeId) throws ResourceNotFoundException;

}
