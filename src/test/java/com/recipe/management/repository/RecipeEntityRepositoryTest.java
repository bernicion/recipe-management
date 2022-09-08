package com.recipe.management.repository;

import com.recipe.management.model.entity.RecipeEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecipeEntityRepositoryTest extends BaseRepositoryUnitTest{

    @Autowired
    RecipeEntityRepository recipeEntityRepository;

    @Test
    void testFindRecipeById() {
        Optional<RecipeEntity> recipeOptional = recipeEntityRepository.findById(1L);
        assertTrue(recipeOptional.isPresent());

        RecipeEntity recipeEntity = recipeOptional.get();
        assertEquals("Limonade", recipeEntity.getName(), "Wrong recipe name");
    }

    @Test
    void testFindAllRecipes(){
        List<RecipeEntity> recipeEntityList = recipeEntityRepository.findAll();
        assertEquals(2,recipeEntityList.size(), "Wrong number of expected recipes" );
    }
}
