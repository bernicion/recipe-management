package com.recipe.management.repository;

import com.recipe.management.model.entity.IngredientEntity;
import com.recipe.management.model.entity.ProductEntity;
import com.recipe.management.model.entity.RecipeEntity;
import com.recipe.management.openapi.model.UnitOfMeasureDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith({SpringExtension.class,})
@DataJpaTest
public abstract class BaseRepositoryUnitTest {
    @Autowired
    RecipeEntityRepository recipeEntityRepository;

    @BeforeAll
    void initH2Data() {
        ProductEntity lemonProduct = createProductEntity("Lemon");
        ProductEntity waterProduct = createProductEntity("Water");
        ProductEntity mintProduct = createProductEntity("Mint");
        ProductEntity meatProduct = createProductEntity("Meat");

        IngredientEntity oneGlassOfLemon = createIngredientEntity(lemonProduct, 1, UnitOfMeasureDto.GLASS);
        IngredientEntity twoGlassesOfWater = createIngredientEntity(waterProduct, 2, UnitOfMeasureDto.GLASS);
        IngredientEntity onePieceOfMint = createIngredientEntity(mintProduct, 1, UnitOfMeasureDto.PIECE);
        IngredientEntity steakMeat = createIngredientEntity(meatProduct, 2, UnitOfMeasureDto.POUND);

        RecipeEntity limonade = createRecipeEntity("Limonade",true,  2, Set.of(oneGlassOfLemon, twoGlassesOfWater, onePieceOfMint));
        RecipeEntity steak = createRecipeEntity("Steak", false, 1, Set.of(steakMeat));
        recipeEntityRepository.saveAll(List.of(
                limonade, steak
        ));
    }

    private RecipeEntity createRecipeEntity(String name, boolean isVegan, Integer servings, Set<IngredientEntity> ingredients) {
        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setName(name);
        recipeEntity.setIsVegan(isVegan);
        recipeEntity.setServings(servings);
        recipeEntity.setIngredients(ingredients);
        return recipeEntity;
    }

    private IngredientEntity createIngredientEntity(ProductEntity productEntity, Integer quantity, UnitOfMeasureDto unitOfMeasure) {
        IngredientEntity ingredientEntity = new IngredientEntity();
        ingredientEntity.setProduct(productEntity);
        ingredientEntity.setQuantity(quantity);
        ingredientEntity.setUnitOfMeasure(unitOfMeasure.getValue());
        return ingredientEntity;
    }

    private ProductEntity createProductEntity(String name) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(name);
        return productEntity;
    }
}
