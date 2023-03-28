package com.api.recipemanagement.services;

import com.api.recipemanagement.domain.Recipe;
import com.api.recipemanagement.exceptions.BadRequestException;
import com.api.recipemanagement.exceptions.ResourceNotFoundException;
import com.api.recipemanagement.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Service
@Transactional
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    RecipeRepository recipeRepository;

    @Override
    public List<Recipe> fetchAllRecipes(Integer userId) throws ResourceNotFoundException {
        return recipeRepository.findAll(userId);
    }

    @Override
    public Recipe fetchRecipeById(Integer userId, Integer recipeId) throws ResourceNotFoundException {
        return recipeRepository.findById(userId, recipeId);
    }

    @Override
    public Recipe addRecipe(Integer userId, String recipeName, Boolean isVegetarian, Integer numberOfPeople, String ingredients, String cookingInstructions) throws BadRequestException {
        int recipeId = recipeRepository.create(userId, recipeName, isVegetarian, numberOfPeople, ingredients, cookingInstructions);
        return recipeRepository.findById(userId, recipeId);
    }

    @Override
    public void updateRecipe(Integer userId, Integer recipeId, Recipe recipe) throws BadRequestException{
        recipeRepository.update(userId, recipeId, recipe);
    }

    @Override
    public void removeRecipe(Integer userId, Integer recipeId) throws ResourceNotFoundException {
        this.fetchRecipeById(userId, recipeId);
        recipeRepository.removeById(userId, recipeId);
    }

}
