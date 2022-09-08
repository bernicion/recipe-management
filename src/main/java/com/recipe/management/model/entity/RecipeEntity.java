package com.recipe.management.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "recipes")
public class RecipeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_recipes_id")
    @SequenceGenerator(name = "seq_recipes_id", sequenceName = "seq_recipes_id", allocationSize = 1)
    private Long id;

    @Column(name = "recipe_name")
    private String name;

    @Column(name = "recipe_servings")
    private Integer servings;

    @Column(name = "recipe_is_vegan")
    private Boolean isVegan;

    @Column(name= "recipe_instruction")
    String instruction;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "recipe_ingedients",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    private Set<IngredientEntity> ingredients;
}
