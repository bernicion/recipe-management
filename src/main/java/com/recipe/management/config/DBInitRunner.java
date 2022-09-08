package com.recipe.management.config;

import com.recipe.management.model.entity.IngredientEntity;
import com.recipe.management.model.entity.ProductEntity;
import com.recipe.management.model.entity.RecipeEntity;
import com.recipe.management.openapi.model.UnitOfMeasureDto;
import com.recipe.management.repository.RecipeEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DBInitRunner implements CommandLineRunner {
    private final RecipeEntityRepository recipeEntityRepository;

    @Override
    public void run(String... args) throws Exception {
        ProductEntity lemonProduct = createProductEntity("Lemon Juice");
        ProductEntity waterProduct = createProductEntity("Water");
        ProductEntity mintProduct = createProductEntity("Mint");
        ProductEntity iceProduct = createProductEntity("Ice");
        ProductEntity sugarProduct = createProductEntity("Sugar");
        ProductEntity meatProduct = createProductEntity("Meat");

        IngredientEntity oneGlassOfLemon = createIngredientEntity(lemonProduct, 1, UnitOfMeasureDto.GLASS);
        IngredientEntity twoGlassesOfWater = createIngredientEntity(waterProduct, 2, UnitOfMeasureDto.GLASS);
        IngredientEntity onePieceOfMint = createIngredientEntity(mintProduct, 1, UnitOfMeasureDto.PIECE);
        IngredientEntity steakMeat = createIngredientEntity(meatProduct, 2, UnitOfMeasureDto.POUND);
        IngredientEntity ice = createIngredientEntity(iceProduct, 5, UnitOfMeasureDto.PIECE);
        IngredientEntity sugar = createIngredientEntity(sugarProduct, 1, UnitOfMeasureDto.PIECE);

        if(recipeEntityRepository.findAll().isEmpty()) {
            RecipeEntity limonade = createRecipeEntity("Limonade", true, 2,
                    Set.of(oneGlassOfLemon, twoGlassesOfWater, onePieceOfMint, ice, sugar), "Combine the lemon juice and sugar, stir into the cold water. Serve over ice.");
            RecipeEntity steak = createRecipeEntity("Steak", false, 1, Set.of(steakMeat), "Medium rare steak should be cooked on medium fire for 30 minutes");
            recipeEntityRepository.saveAll(List.of(
                    limonade, steak
            ));
        }
    }

    private RecipeEntity createRecipeEntity(String name, boolean isVegan, Integer servings, Set<IngredientEntity> ingredients, String instruction) {
        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setName(name);
        recipeEntity.setIsVegan(isVegan);
        recipeEntity.setServings(servings);
        recipeEntity.setIngredients(ingredients);
        recipeEntity.setInstruction(instruction);
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
