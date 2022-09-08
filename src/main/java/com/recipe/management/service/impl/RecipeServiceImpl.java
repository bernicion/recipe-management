package com.recipe.management.service.impl;

import com.recipe.management.mapping.RecipeMapper;
import com.recipe.management.model.entity.RecipeEntity;
import com.recipe.management.openapi.model.RecipeDto;
import com.recipe.management.openapi.model.RecipeSearchCriteriaDto;
import com.recipe.management.repository.RecipeEntityRepository;
import com.recipe.management.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Implementation of the RecipeService interface
 *
 * @author Ion Bernic
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
    private final RecipeEntityRepository recipeEntityRepository;

    private final RecipeMapper recipeMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RecipeDto> findAllRecipesBySearchCriteria(RecipeSearchCriteriaDto recipeSearchCriteriaDto, Pageable pageable) {
        Specification<RecipeEntity> specification = new RecipeEntitySpecification(recipeSearchCriteriaDto);
        return recipeEntityRepository.findAll(specification, pageable)
                .stream()
                .map(recipeMapper::toRecipeDto)
                .toList();
    }

    @Override
    public List<RecipeDto> getAllRecipes(Pageable pageable) {
        return recipeEntityRepository.findAll(pageable)
                .stream()
                .map(recipeMapper::toRecipeDto)
                .toList();
    }

    @Override
    public RecipeDto saveRecipe(RecipeDto recipeDto) {
        RecipeEntity recipeEntity = recipeMapper.toRecipeEntity(recipeDto);
        RecipeEntity savedRecipe = recipeEntityRepository.saveAndFlush(recipeEntity);
        return recipeMapper.toRecipeDto(savedRecipe);
    }

    @Override
    public RecipeDto updateRecipe(Long id, RecipeDto recipeDto) {
        recipeEntityRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("No such recipe with id %s", id)));
        log.debug("Updating recipe with id={}", id);

        RecipeEntity recipeEntity = recipeMapper.toRecipeEntity(recipeDto);
        RecipeEntity updatedRecipe = recipeEntityRepository.saveAndFlush(recipeEntity);
        return recipeMapper.toRecipeDto(updatedRecipe);
    }

    @Override
    public void deleteRecipe(Long id) {
        RecipeEntity dbRecipeEntity = recipeEntityRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("No such recipe with id %s", id)));
        log.debug("Deleting recipe with id ={}", id);
        recipeEntityRepository.delete(dbRecipeEntity);
    }

    @Override
    public RecipeDto getRecipeById(Long id) {
        log.debug("Extracting recipe with id={}", id);
        RecipeEntity recipeEntity = recipeEntityRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("No such recipe with id %s", id)));
        return recipeMapper.toRecipeDto(recipeEntity);
    }
}
