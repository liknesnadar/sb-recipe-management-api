package com.api.recipemanagement.repositories;

import com.api.recipemanagement.domain.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class RecipeRepositoryImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private RecipeRepositoryImpl recipeRepositoryImpl = new RecipeRepositoryImpl();

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

        when(jdbcTemplate.query(SQL_FIND_ALL,new Object[]{2001}, recipeRowMapper)).thenReturn(recipeList);
        when(jdbcTemplate.queryForObject(SQL_FIND_BY_ID,new Object[]{2001, 1002}, recipeRowMapper)).thenReturn(recipe2);

    }


    @Test
    void findAll() {
        assertEquals(3,recipeRepositoryImpl.findAll(2001).size());
        assertEquals(1001,recipeRepositoryImpl.findAll(2001).get(0).getRecipeId());
        assertEquals(1002,recipeRepositoryImpl.findAll(2001).get(1).getRecipeId());
        assertEquals(1003,recipeRepositoryImpl.findAll(2001).get(2).getRecipeId());
    }

    @Test
    void findById() {
        assertEquals(1002,recipeRepositoryImpl.findById(2001,1002).getRecipeId());
        assertEquals(2001,recipeRepositoryImpl.findById(2001,1002).getUserId());
        assertEquals("Paneer Masala",recipeRepositoryImpl.findById(2001,1002).getRecipeName());
    }

    @Test
    void create() {
        Integer recipeId = recipeRepositoryImpl.create(2001,"Mushroom Masala",true,5,
                "Mushroom,Masala","Cook mushroom with masala");
        System.out.println(recipeId);
    }

    @Test
    void update() {
        Recipe recipe = new Recipe(1002,2001,"Paneer Butter Masala","30-03-2023 22:52",true,5,
                "Paneer,Butter,Masala","Cook paneer with masala");
        recipeRepositoryImpl.update(2001,1002,recipe);
    }

    @Test
    void removeById() {
        recipeRepositoryImpl.removeById(2001,1002);
    }

}