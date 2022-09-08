package com.recipe.management.integrationtest;

import com.recipe.management.RecipeManagementApplication;
import com.recipe.management.openapi.model.IngredientDto;
import com.recipe.management.openapi.model.RecipeDto;
import com.recipe.management.openapi.model.UnitOfMeasureDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = RecipeManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RecipeIntegrationTest {
    @Autowired
    protected TestRestTemplate restTemplate;

    @Test
    @Order(1)
    void testGetRecipes() {
        var response = restTemplate.getForEntity("/api/recipes?page=0&size=10", RecipeDto[].class);
        var recipes = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Wrong status code");
        assertEquals(2, recipes.length, "Wrong number of recipes found");
        assertEquals("Limonade", recipes[0].getName(), "Wrong name of recipe");
        assertEquals("Steak", recipes[1].getName(), "Wrong name of recipe");
    }

    @Test
    @Order(2)
    void testGetRecipeById() {
        var response = restTemplate.getForEntity("/api/recipes/1", RecipeDto.class);
        var recipe = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Wrong status code");
        assertEquals("Limonade", recipe.getName(), "Wrong name of recipe");
    }

    @Test
    @Order(3)
    void testDeleteRecipeById() {
        var response = restTemplate.exchange("/api/recipes/2", HttpMethod.DELETE,
                new HttpEntity<>(new RecipeDto(), new HttpHeaders()), Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Wrong status code");
    }

    @Test
    @Order(4)
    void testGetRecipesAfterDelete() {
        var response = restTemplate.getForEntity("/api/recipes", RecipeDto[].class);
        var recipes = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Wrong status code");
        assertEquals(1, recipes.length, "Wrong number of recipes found");
        assertEquals("Limonade", recipes[0].getName(), "Wrong name of recipe");
    }

    @Test
    @Order(5)
    void testAddRecipe() {
        RecipeDto recipeDto = recipeDtoBody(null, "Pizza", true, 4,
                "All you need to prepare the simplest Pizza at home is all-purpose flour, dry yeast, warm water",
                ingredientDto(1, UnitOfMeasureDto.PIECE, "Cheese"));
        var response = restTemplate.postForEntity("/api/recipes", recipeDto, RecipeDto.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Wrong status code");
        var recipe = response.getBody();
        assertEquals(3, recipe.getId(), "Wrong id of recipe");
        assertEquals("Pizza", recipe.getName(), "Wrong name of recipe");
        List<IngredientDto> ingredients = recipe.getIngredients();
        assertEquals(1, ingredients.size(), "Wrong number of ingredients");
        assertEquals("Cheese", ingredients.get(0).getName(), "Wrong ingredient name");
    }

    @Test
    @Order(6)
    void testUpdateRecipeAddIngredient() {
        var lastRecipeId = 3L;
        RecipeDto recipeDto = recipeDtoBody(lastRecipeId, "Pizza", true, 4,
                "All you need to prepare the simplest Pizza at home is all-purpose flour, dry yeast, warm water",
                ingredientDto(1, UnitOfMeasureDto.PIECE, "Cheese"),
                ingredientDto(2, UnitOfMeasureDto.PIECE, "Tomatoes"));
        restTemplate.put("/api/recipes/" + lastRecipeId, recipeDto);
    }

    @Test
    @Order(7)
    void testGetUpdatedRecipeById() {
        var lastRecipeId = 3L;
        var response = restTemplate.getForEntity("/api/recipes/" + lastRecipeId, RecipeDto.class);
        var recipe = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Wrong status code");
        assertEquals("Pizza", recipe.getName(), "Wrong name of recipe");

        List<IngredientDto> ingredients = recipe.getIngredients();
        assertEquals(2, ingredients.size(), "Wrong number of ingredients");
        assertTrue(List.of("Cheese", "Tomatoes").containsAll(ingredients.stream().map(IngredientDto::getName).toList()));
    }

    private RecipeDto recipeDtoBody(Long id, String name, Boolean isVegan, Integer servings, String instructions, IngredientDto... ingredients) {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(id);
        recipeDto.setName(name);
        recipeDto.setIsVegan(isVegan);
        recipeDto.setServings(servings);
        recipeDto.setInstruction(instructions);
        recipeDto.setIngredients(List.of(ingredients));
        return recipeDto;
    }

    private IngredientDto ingredientDto(Integer quantity, UnitOfMeasureDto unitOfMeasureDto, String ingredientName) {
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setName(ingredientName);
        ingredientDto.setQuantity(quantity);
        ingredientDto.setUnitOfMeasure(unitOfMeasureDto);
        return ingredientDto;
    }
}
