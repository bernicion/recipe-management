package com.recipe.management.controller;

import com.recipe.management.openapi.model.RecipeDto;
import com.recipe.management.openapi.model.RecipeSearchCriteriaDto;
import com.recipe.management.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.ResultActions;

import static com.recipe.management.mock.RecipeManagementTestMock.mockRecipeDto;
import static com.recipe.management.mock.RecipeManagementTestMock.mockRecipesList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RecipeControllerTest extends BaseControllerUnitTest {

    @MockBean
    private RecipeService recipeService;

    @Test
    void testCreateNewRecipe() throws Exception {
        when(recipeService.saveRecipe(any(RecipeDto.class))).thenReturn(mockRecipeDto());
        String jsonBody = jsonMapper.writeValueAsString(new RecipeDto());
        ResultActions result = performPOSTRequest("/api/recipes", jsonBody);

        result.andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://a.recipe.management:8080/api/recipes/1500"))
                .andExpect(jsonPath("$.id").value("1500"))
                .andExpect(jsonPath("$.name").value("Pizza"));
    }

    @Test
    void testUpdateRecipe() throws Exception {
        when(recipeService.updateRecipe(eq(1500L), any(RecipeDto.class))).thenReturn(mockRecipeDto());
        String jsonBody = jsonMapper.writeValueAsString(new RecipeDto());
        ResultActions result = performPUTRequest("/api/recipes/", 1500L, jsonBody);

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1500"))
                .andExpect(jsonPath("$.name").value("Pizza"));
    }

    @Test
    void testGetRecipeById() throws Exception {
        when(recipeService.getRecipeById(1500L)).thenReturn(mockRecipeDto());
        ResultActions result = performGETRequestWithPathParam("/api/recipes/", 1500L);

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1500"))
                .andExpect(jsonPath("$.name").value("Pizza"));
    }

    @Test
    void testDeleteRecipeById() throws Exception {
        ResultActions result = performDeleteRequestWithPathParam("/api/recipes/", 1500L);
        result.andExpect(status().isOk());
    }

    @Test
    void testGetAllRecipes() throws Exception {
        when(recipeService.getAllRecipes(any(Pageable.class))).thenReturn(mockRecipesList());
        ResultActions result = performGETRequestWithPathParam("/api/recipes", null);

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value("1500"))
                .andExpect(jsonPath("$.[0].name").value("Pizza"))
                .andExpect(jsonPath("$.[1].id").value("1600"))
                .andExpect(jsonPath("$.[1].name").value("Pasta"));
    }

    @Test
    void testGetAllRecipesWithPagination() throws Exception {
        when(recipeService.getAllRecipes(any(Pageable.class))).thenReturn(mockRecipesList());
        ResultActions result = performGETRequestWithPagination("/api/recipes", 0, 50);

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value("1500"))
                .andExpect(jsonPath("$.[0].name").value("Pizza"))
                .andExpect(jsonPath("$.[1].id").value("1600"))
                .andExpect(jsonPath("$.[1].name").value("Pasta"));
    }

    @Test
    void testGetAllRecipesByCriteria() throws Exception {
        when(recipeService.findAllRecipesBySearchCriteria(any(RecipeSearchCriteriaDto.class), any(PageRequest.class))).thenReturn(mockRecipesList());
        String jsonBody = jsonMapper.writeValueAsString(new RecipeSearchCriteriaDto());
        ResultActions result = performPOSTRequest("/api/recipes/filter", jsonBody);

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value("1500"))
                .andExpect(jsonPath("$.[0].name").value("Pizza"))
                .andExpect(jsonPath("$.[1].id").value("1600"))
                .andExpect(jsonPath("$.[1].name").value("Pasta"));
    }
}
