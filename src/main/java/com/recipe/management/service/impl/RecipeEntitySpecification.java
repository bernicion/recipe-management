package com.recipe.management.service.impl;

import com.recipe.management.model.entity.IngredientEntity;
import com.recipe.management.model.entity.ProductEntity;
import com.recipe.management.model.entity.RecipeEntity;
import com.recipe.management.openapi.model.IngredientLookupDto;
import com.recipe.management.openapi.model.RecipeSearchCriteriaDto;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Optional.ofNullable;

/**
 * Implementation of the Specification interface used to map a RecipeSearchCriteriaDto to Predicate for DB extraction
 *
 * @author Ion Bernic
 */
public record RecipeEntitySpecification(RecipeSearchCriteriaDto recipeSearchCriteriaDto) implements Specification<RecipeEntity> {

    @Override
    public Predicate toPredicate(Root<RecipeEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        var isVegan = recipeSearchCriteriaDto.getIsVegan();
        Predicate isVeganPredicate = ofNullable(isVegan)
                .map(b -> equals(cb, root.get("isVegan"), isVegan))
                .orElse(null);

        var servings = recipeSearchCriteriaDto.getServings();
        Predicate servingsPredicate = ofNullable(servings)
                .map(b -> equals(cb, root.get("servings"), servings))
                .orElse(null);

        var instruction = recipeSearchCriteriaDto.getInstruction();
        Predicate instrPredicate = ofNullable(instruction)
                .map(b -> contains(cb, root.get("instruction"), instruction))
                .orElse(null);

        List<Predicate> ingredientPredicates = ingredientPredicates(root, cb);
        if (!ingredientPredicates.isEmpty()) {
            query.distinct(true);
        }

        List<Predicate> predicates = new ArrayList<>(ingredientPredicates);
        ofNullable(isVeganPredicate).ifPresent(predicates::add);
        ofNullable(servingsPredicate).ifPresent(predicates::add);
        ofNullable(instrPredicate).ifPresent(predicates::add);
        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
    }

    private List<Predicate> ingredientPredicates(Root<RecipeEntity> root, CriteriaBuilder cb) {
        var ingredients = recipeSearchCriteriaDto.getIngredients();
        if (ingredients == null || ingredients.isEmpty()) {
            return Collections.emptyList();
        }
        Join<RecipeEntity, IngredientEntity> ingredientJoin = root.join("ingredients", JoinType.LEFT);

        Join<IngredientEntity, ProductEntity> productJoin = ingredientJoin.join("product", JoinType.LEFT);

        List<Predicate> resultPredicates = new ArrayList<>();
        List<String> includeIngredients =
                ingredients.stream()
                        .filter(IngredientLookupDto::getInclude)
                        .map(IngredientLookupDto::getName)
                        .toList();
        if (!includeIngredients.isEmpty()) {
            resultPredicates.add(productJoin.get("name").in(includeIngredients));
        }
        List<String> doNotIncludeIngredients =
                ingredients.stream()
                        .filter(ig -> !ig.getInclude())
                        .map(IngredientLookupDto::getName)
                        .toList();
        if (!doNotIncludeIngredients.isEmpty()) {
            resultPredicates.add(productJoin.get("name").in(doNotIncludeIngredients).not());
        }
        return resultPredicates;
    }

    private Predicate equals(CriteriaBuilder cb, Path<Object> field, Object value) {
        return cb.equal(field, value);
    }

    private Predicate contains(CriteriaBuilder cb, Path<String> field, String searchTerm) {
        return cb.like(cb.lower(field), "%" + searchTerm.toLowerCase() + "%");
    }
}
