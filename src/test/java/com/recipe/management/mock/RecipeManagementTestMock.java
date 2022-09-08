package com.recipe.management.mock;

import com.recipe.management.model.entity.RecipeEntity;
import com.recipe.management.openapi.model.RecipeDto;

import java.util.List;

public class RecipeManagementTestMock {
    private RecipeManagementTestMock() {
    }

    public static RecipeDto mockRecipeDto() {
        return createRecipeDto(1500L, "Pizza");
    }

    public static List<RecipeDto> mockRecipesList() {
        RecipeDto pasta = createRecipeDto(1600L, "Pasta");
        return List.of(mockRecipeDto(), pasta);
    }

    public static RecipeDto createRecipeDto(Long id, String name) {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(id);
        recipeDto.setName(name);
        return recipeDto;
    }

    public static List<RecipeEntity> mockRecipeEntities() {
        RecipeEntity juice = createRecipeEntity(110L, "Juice");
        return List.of(mockRecipeEntity(), juice);
    }

    public static RecipeEntity mockRecipeEntity() {
        return createRecipeEntity(100L, "Steak");
    }

    public static RecipeEntity createRecipeEntity(Long id, String name) {
        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setId(id);
        recipeEntity.setName(name);
        return recipeEntity;
    }
}
