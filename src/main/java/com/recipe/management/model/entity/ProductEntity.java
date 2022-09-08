package com.recipe.management.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_products_id")
    @SequenceGenerator(name = "seq_products_id", sequenceName = "seq_products_id", allocationSize = 1)
    private Long id;

    @Column(name = "product_name")
    private String name;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private IngredientEntity ingredient;
}
