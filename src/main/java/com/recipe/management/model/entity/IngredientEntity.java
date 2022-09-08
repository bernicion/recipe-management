package com.recipe.management.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "ingredients")
public class IngredientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ingredients_id")
    @SequenceGenerator(name = "seq_ingredients_id", sequenceName = "seq_ingredients_id", allocationSize = 1)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "ingredient_products",
            joinColumns = {@JoinColumn(name = "ingredient_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")})
    private ProductEntity product;

    @Column(name = "ingredient_quantity")
    private Integer quantity;

    @Column(name = "ingredient_unit_of_measure")
    private String unitOfMeasure;

    @ManyToMany(mappedBy = "ingredients", cascade = CascadeType.ALL)
    Set<RecipeEntity> recipes;

    public void setProduct(ProductEntity productEntity){
        productEntity.setIngredient(this);
        this.product = productEntity;
    }
}
