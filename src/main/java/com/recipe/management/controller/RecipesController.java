package com.recipe.management.controller;

import com.recipe.management.openapi.api.RecipesApi;
import com.recipe.management.openapi.model.RecipeDto;
import com.recipe.management.openapi.model.RecipeSearchCriteriaDto;
import com.recipe.management.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
/**
 * Implementation class of the RecipesApi defined from OpenAPI generation tool
 *
 * @author Ion Bernic
 * */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RecipesController implements RecipesApi {

    private final RecipeService recipeService;

    @Override
    public ResponseEntity<List<RecipeDto>> filterRecipesByCriteria(RecipeSearchCriteriaDto recipeSearchCriteriaDto, Integer page, Integer size) {
        return ResponseEntity.ok(recipeService.findAllRecipesBySearchCriteria(recipeSearchCriteriaDto, PageRequest.of(page, size)));
    }

    @Override
    public ResponseEntity<List<RecipeDto>> getRecipes(Integer page, Integer size) {
        return ResponseEntity.ok(recipeService.getAllRecipes(PageRequest.of(page, size)));
    }

    @Override
    public ResponseEntity<RecipeDto> addRecipe(RecipeDto recipeDto) {
        RecipeDto savedRecipeDto = recipeService.saveRecipe(recipeDto);
        return ResponseEntity.created(buildResponseUriForRecipe(savedRecipeDto)).body(savedRecipeDto);
    }

    private URI buildResponseUriForRecipe(RecipeDto recipeDto) {
        return URI.create(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RecipesController.class)
                .getRecipeById(recipeDto.getId())).withSelfRel().getHref());
    }

    @Override
    public ResponseEntity<RecipeDto> getRecipeById(Long id) {
        return ResponseEntity.ok(recipeService.getRecipeById(id));
    }

    @Override
    public ResponseEntity<RecipeDto> updateRecipeById(Long id, RecipeDto recipeDto) {
        return ResponseEntity.ok(recipeService.updateRecipe(id, recipeDto));
    }

    @Override
    public ResponseEntity<Void> deleteRecipeById(Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.ok().build();
    }
}
