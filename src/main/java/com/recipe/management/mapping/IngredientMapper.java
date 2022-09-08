package com.recipe.management.mapping;

import com.recipe.management.model.entity.IngredientEntity;
import com.recipe.management.openapi.model.IngredientDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    @Mapping(target = "name", source = "ingredientEntity.product.name")
    IngredientDto toIngredientDto(IngredientEntity ingredientEntity);

    @Mapping(target = "product.name", source = "ingredientDto.name")
    IngredientEntity toIngredientEntity(IngredientDto ingredientDto);
}
