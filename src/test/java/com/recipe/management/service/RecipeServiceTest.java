package com.recipe.management.service;

import com.recipe.management.mapping.RecipeMapper;
import com.recipe.management.model.entity.RecipeEntity;
import com.recipe.management.openapi.model.RecipeDto;
import com.recipe.management.repository.RecipeEntityRepository;
import com.recipe.management.service.impl.RecipeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static com.recipe.management.mock.RecipeManagementTestMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {
    @InjectMocks
    RecipeServiceImpl recipeService;
    @Mock
    RecipeEntityRepository recipeEntityRepository;
    @Spy
    RecipeMapper recipeMapper = Mappers.getMapper(RecipeMapper.class);
    @Captor
    ArgumentCaptor<RecipeEntity> recipeEntityArgumentCaptor;

    @Test
    void testGetRecipeById() {
        when(recipeEntityRepository.findById(100L)).thenReturn(Optional.of(mockRecipeEntity()));
        RecipeDto recipeDto = recipeService.getRecipeById(100L);
        assertRecipeDtoProperties(recipeDto, 100L, "Steak");
    }

    @Test
    void testGetAllRecipes() {
        var pageable = PageRequest.of(0, 50);
        when(recipeEntityRepository.findAll(pageable)).thenReturn(new PageImpl<>(mockRecipeEntities()));
        List<RecipeDto> recipes = recipeService.getAllRecipes(pageable);
        assertEquals(2, recipes.size(), "Wrong number of expected recipes");
        assertRecipeDtoProperties(recipes.get(0), 100L, "Steak");
        assertRecipeDtoProperties(recipes.get(1), 110L, "Juice");
    }

    @Test
    void testDeleteRecipeById() {
        RecipeEntity recipeEntity = mockRecipeEntity();
        when(recipeEntityRepository.findById(100L)).thenReturn(Optional.of(recipeEntity));
        recipeService.deleteRecipe(100L);
        verify(recipeEntityRepository, times(1)).delete(recipeEntity);
    }

    @Test
    void testAddRecipe() {
        recipeService.saveRecipe(mockRecipeDto());
        Mockito.verify(recipeEntityRepository).saveAndFlush(recipeEntityArgumentCaptor.capture());
        RecipeEntity recipeEntity = recipeEntityArgumentCaptor.getValue();

        RecipeDto recipeDto = recipeMapper.toRecipeDto(recipeEntity);
        assertRecipeDtoProperties(recipeDto, 1500L, "Pizza");
    }

    @Test
    void testUpdateRecipe() {
        when(recipeEntityRepository.findById(1500L)).thenReturn(Optional.of(mockRecipeEntity()));
        recipeService.updateRecipe(1500L, mockRecipeDto());
        Mockito.verify(recipeEntityRepository).saveAndFlush(recipeEntityArgumentCaptor.capture());
        RecipeEntity recipeEntity = recipeEntityArgumentCaptor.getValue();

        RecipeDto recipeDto = recipeMapper.toRecipeDto(recipeEntity);
        assertRecipeDtoProperties(recipeDto, 1500L, "Pizza");
    }

    private void assertRecipeDtoProperties(RecipeDto recipeDto, Long id, String name) {
        assertEquals(id, recipeDto.getId(), "Wrong recipe id");
        assertEquals(name, recipeDto.getName(), "Wrong recipe name");
    }
}
