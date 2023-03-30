package com.api.recipemanagement.services;

import com.api.recipemanagement.domain.Recipe;
import com.api.recipemanagement.repositories.RecipeRepository;
import com.api.recipemanagement.repositories.RecipeRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class RecipeServiceImplTest {

    @Mock
    private RecipeRepositoryImpl recipeRepositoryImpl;

    @InjectMocks
    private RecipeServiceImpl recipeServiceImpl = new RecipeServiceImpl();

    @BeforeEach
    void setMockOutput() {

        Recipe recipe1 = new Recipe(1001,2001,"Mushroom Masala","30-03-2023 22:22",true,5,
                "Mushroom,Masala","Cook mushroom with masala");
        Recipe recipe2 = new Recipe(1002,2001,"Paneer Masala","30-03-2023 22:52",true,5,
                "Paneer,Masala","Cook paneer with masala");
        Recipe recipe3 = new Recipe(1003,2001,"Chicken Masala","30-03-2023 22:54",true,5,
                "Chicken,Masala","Cook chicken with masala");

        List<Recipe> recipeList = new ArrayList<>();
        recipeList.add(recipe1);
        recipeList.add(recipe2);
        recipeList.add(recipe3);

        when(recipeRepositoryImpl.findById(2001,1002)).thenReturn(recipe2);
        when(recipeRepositoryImpl.findAll(2001)).thenReturn(recipeList);
        when(recipeRepositoryImpl.create(recipe1.getUserId(), recipe1.getRecipeName(),recipe1.getVegetarian(),recipe1.getNumberOfPeople(),
                recipe1.getIngredients(),recipe1.getCookingInstructions())).thenReturn(recipe1.getRecipeId());

    }

    @Test
    void fetchAllRecipes() {
        assertEquals(3, recipeServiceImpl.fetchAllRecipes(2001).size());
        assertEquals(1001, recipeServiceImpl.fetchAllRecipes(2001).get(0).getRecipeId());
        assertEquals(1002, recipeServiceImpl.fetchAllRecipes(2001).get(1).getRecipeId());
        assertEquals(1003, recipeServiceImpl.fetchAllRecipes(2001).get(2).getRecipeId());
    }

    @Test
    void fetchRecipeById() {
        assertEquals(1002,recipeServiceImpl.fetchRecipeById(2001,1002).getRecipeId());
        assertEquals(2001,recipeServiceImpl.fetchRecipeById(2001,1002).getUserId());
        assertEquals("Paneer Masala",recipeServiceImpl.fetchRecipeById(2001,1002).getRecipeName());
    }

    @Test
    void addRecipe() {
        Recipe recipe = recipeServiceImpl.addRecipe(2001,"Mushroom Masala",true,5,
                "Mushroom,Masala","Cook mushroom with masala");
        System.out.println(recipe);
        //assertEquals(1001,recipe);
    }

    @Test
    void updateRecipe() {
        Recipe recipe = new Recipe(1002,2001,"Paneer Butter Masala","30-03-2023 22:52",true,5,
                "Paneer,Butter,Masala","Cook paneer with masala");
        recipeServiceImpl.updateRecipe(2001,1002,recipe);
    }

    @Test
    void removeRecipe() {
        recipeServiceImpl.removeRecipe(2001,1002);
    }
}