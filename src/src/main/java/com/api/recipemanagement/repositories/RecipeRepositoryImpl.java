package com.api.recipemanagement.repositories;

import com.api.recipemanagement.domain.Recipe;
import com.api.recipemanagement.exceptions.BadRequestException;
import com.api.recipemanagement.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class RecipeRepositoryImpl implements RecipeRepository {

    private static final String SQL_FIND_ALL = "SELECT RECIPE_ID, USER_ID, RECIPE_NAME, CREATED_DATE_TIME, IS_VEGETARIAN, " +
            "NUMBER_OF_PEOPLE, INGREDIENTS, COOKING_INSTRUCTIONS " +
            "FROM RECIPES WHERE USER_ID = ? " +
            "ORDER BY RECIPE_ID";

    private static final String SQL_FIND_BY_ID = "SELECT RECIPE_ID, USER_ID, RECIPE_NAME, CREATED_DATE_TIME, IS_VEGETARIAN, " +
            "NUMBER_OF_PEOPLE, INGREDIENTS, COOKING_INSTRUCTIONS " +
            "FROM RECIPES WHERE USER_ID = ? AND RECIPE_ID = ? " +
            "ORDER BY RECIPE_ID";

    private static final String SQL_CREATE = "INSERT INTO RECIPES (RECIPE_ID, USER_ID, RECIPE_NAME, CREATED_DATE_TIME, IS_VEGETARIAN, " +
            "NUMBER_OF_PEOPLE, INGREDIENTS, COOKING_INSTRUCTIONS) " +
            "VALUES(NEXTVAL('RECIPES_SEQ'), ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE = "UPDATE RECIPES SET RECIPE_NAME = ?, IS_VEGETARIAN = ?, NUMBER_OF_PEOPLE = ?, " +
            "INGREDIENTS = ?, COOKING_INSTRUCTIONS = ? " +
            "WHERE USER_ID = ? AND RECIPE_ID = ?";

    private static final String SQL_DELETE = "DELETE FROM RECIPES " +
            "WHERE USER_ID = ? AND RECIPE_ID = ?";

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd‐MM‐yyyy HH:mm");

    @Autowired
    JdbcTemplate jdbcTemplate;

    private final RowMapper<Recipe> recipeRowMapper = ((rs, rowNum) -> {
        return new Recipe(rs.getInt("RECIPE_ID"),
                rs.getInt("USER_ID"),
                rs.getString("RECIPE_NAME"),
                rs.getTimestamp("CREATED_DATE_TIME").toLocalDateTime().format(dateTimeFormatter),
                rs.getBoolean("IS_VEGETARIAN"),
                rs.getInt("NUMBER_OF_PEOPLE"),
                rs.getString("INGREDIENTS"),
                rs.getString("COOKING_INSTRUCTIONS"));
    });

    @Override
    public List<Recipe> findAll(Integer userId) throws ResourceNotFoundException {
        List<Recipe> recipes = jdbcTemplate.query(SQL_FIND_ALL, new Object[]{userId}, recipeRowMapper);
        if (recipes.size() > 0)
            return recipes;
        else
            throw new ResourceNotFoundException("No recipes found!");
    }

    @Override
    public Recipe findById(Integer userId, Integer recipeId) throws ResourceNotFoundException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{userId, recipeId}, recipeRowMapper);
        }
        catch (Exception e) {
            throw new ResourceNotFoundException("Recipe not found!");
        }
    }

    @Override
    public Integer create(Integer userId, String recipeName, Boolean isVegetarian, Integer numberOfPeople,
                          String ingredients, String cookingInstructions) throws BadRequestException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps1 = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps1.setInt(1, userId);
                ps1.setString(2, recipeName);
                ps1.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                ps1.setBoolean(4, isVegetarian);
                ps1.setInt(5, numberOfPeople);
                ps1.setString(6, ingredients);
                ps1.setString(7, cookingInstructions);
                return ps1;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("RECIPE_ID");
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException("Invalid request");
        }
    }

    @Override
    public void update(Integer userId, Integer recipeId, Recipe recipe) throws BadRequestException {
        try {
            int count = jdbcTemplate.update(SQL_UPDATE, recipe.getRecipeName(), recipe.getVegetarian(), recipe.getNumberOfPeople(),
                    recipe.getIngredients(), recipe.getCookingInstructions(), userId, recipeId);
            if (count == 0) {
                throw new ResourceNotFoundException("Recipe not found!");
            }
        }
        catch (Exception e) {
            throw new BadRequestException("Invalid request");
        }
    }

    @Override
    public void removeById(Integer userId, Integer recipeId) throws ResourceNotFoundException {
        try {
            int count = jdbcTemplate.update(SQL_DELETE, userId, recipeId);
            if (count == 0)
                throw new ResourceNotFoundException("Recipe not found!");
        }
        catch (Exception e) {
            throw new BadRequestException("Invalid request");
        }
    }

}
