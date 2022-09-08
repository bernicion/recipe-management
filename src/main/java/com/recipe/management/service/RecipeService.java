package com.recipe.management.service;

import com.recipe.management.openapi.model.RecipeDto;
import com.recipe.management.openapi.model.RecipeSearchCriteriaDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Interface to define all recipe management functions
 *
 * @author Ion Bernic
 */
public interface RecipeService {
    /**
     * Method used to return list of Recipes based on search criteria object and pagination
     *
     * @param recipeSearchCriteriaDto search criteria objects used for filtering
     * @param pageable                pagination object used limit extraction amount of recipes
     * @return list of RecipeDto objects
     */
    List<RecipeDto> findAllRecipesBySearchCriteria(RecipeSearchCriteriaDto recipeSearchCriteriaDto, Pageable pageable);

    /**
     * Method used to return list of Recipes based on pagination
     *
     * @param pageable pagination object used limit extraction amount of recipes
     * @return list of RecipeDto objects
     */
    List<RecipeDto> getAllRecipes(Pageable pageable);

    /**
     * Method used to save a new recipe
     *
     * @param recipeDto object to be saved
     * @return saved recipeDto object
     */
    RecipeDto saveRecipe(RecipeDto recipeDto);

    /**
     * Method used to update an existing recipe
     *
     * @param id        id of the recipe to be updated
     * @param recipeDto object to be update
     * @return updated recipeDto object
     */
    RecipeDto updateRecipe(Long id, RecipeDto recipeDto);

    /**
     * Method used to delete a recipe
     *
     * @param id id of the recipe to be deleted
     */
    void deleteRecipe(Long id);

    /**
     * Method used to extract an existing recipe
     *
     * @param id id of the recipe to be extracted
     * @return extracted  recipeDto object
     */
    RecipeDto getRecipeById(Long id);
}
