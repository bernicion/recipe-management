package com.recipe.management.mapping;

import com.recipe.management.model.entity.RecipeEntity;
import com.recipe.management.openapi.model.RecipeDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {IngredientMapper.class} )
public interface RecipeMapper {

    RecipeDto toRecipeDto(RecipeEntity recipeEntity);

    RecipeEntity toRecipeEntity(RecipeDto recipeDto);
}
