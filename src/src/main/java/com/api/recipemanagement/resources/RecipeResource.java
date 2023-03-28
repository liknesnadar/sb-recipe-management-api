package com.api.recipemanagement.resources;

import com.api.recipemanagement.domain.Recipe;
import com.api.recipemanagement.services.RecipeService;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recipes")
public class RecipeResource {

    @Autowired
    RecipeService recipeService;

    Logger logger = LoggerFactory.getLogger(RecipeResource.class);

    @GetMapping("")
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    public ResponseEntity<List<Recipe>> getAllRecipes(HttpServletRequest request) {
        int userId = (Integer) request.getAttribute("userId");
        logger.info("REQUEST :: getAllRecipes for userId: " + userId);
        List<Recipe> recipes = recipeService.fetchAllRecipes(userId);
        logger.info("RESPONSE :: " + recipes.size() + " recipes returned for userId: " + userId);
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<Recipe> getRecipeById(HttpServletRequest request,
                                                @PathVariable("recipeId") Integer recipeId) {
        int userId = (Integer) request.getAttribute("userId");
        logger.info("REQUEST :: getRecipe for userId: " + userId + " and recipeId: " + recipeId);
        Recipe recipe = recipeService.fetchRecipeById(userId, recipeId);
        logger.info("RESPONSE :: recipe returned for userId: " + userId + " and recipeId: " + recipeId);
        return new ResponseEntity<>(recipe, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Map<String, String>> createRecipe(HttpServletRequest request,
                                               @RequestBody Map<String, Object> recipeMap) {
        int userId = (Integer) request.getAttribute("userId");
        logger.info("REQUEST :: createRecipe for userId: " + userId);
        String recipeName = (String) recipeMap.get("recipeName");
        Boolean isVegetarian = (Boolean) recipeMap.get("isVegetarian");
        int numberOfPeople = (Integer) recipeMap.get("numberOfPeople");
        String ingredients = (String) recipeMap.get("ingredients");
        String cookingInstructions = (String) recipeMap.get("cookingInstructions");
        Recipe recipe = recipeService.addRecipe(userId, recipeName, isVegetarian, numberOfPeople, ingredients, cookingInstructions);
        logger.info("RESPONSE :: recipe created for userId: " + userId + " with recipeId: " + recipe.getRecipeId());
        Map<String, String> map = new HashMap<>();
        map.put("message", "recipe created with recipe Id: " + recipe.getRecipeId());
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<Map<String, String>> updateRecipe(HttpServletRequest request,
                                                             @PathVariable("recipeId") Integer recipeId,
                                                             @RequestBody Recipe recipe) {
        int userId = (Integer) request.getAttribute("userId");
        logger.info("REQUEST :: updateRecipe for userId: " + userId + " and recipeId: " + recipeId);
        recipeService.updateRecipe(userId, recipeId, recipe);
        logger.info("RESPONSE :: recipe updated for userId: " + userId + " with recipeId: " + recipeId);
        Map<String, String> map = new HashMap<>();
        map.put("message", "recipe updated with recipe Id: " + recipeId);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<Map<String, String>> updateRecipe(HttpServletRequest request,
                                                             @PathVariable("recipeId") Integer recipeId) {
        int userId = (Integer) request.getAttribute("userId");
        logger.info("REQUEST :: deleteRecipe for userId: " + userId + " and recipeId: " + recipeId);
        recipeService.removeRecipe(userId, recipeId);
        logger.info("RESPONSE :: recipe deleted for userId: " + userId + " with recipeId: " + recipeId);
        Map<String, String> map = new HashMap<>();
        map.put("message", "recipe deleted with recipe Id: " + recipeId);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
