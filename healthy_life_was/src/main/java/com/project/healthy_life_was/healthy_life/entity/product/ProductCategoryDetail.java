package com.project.healthy_life_was.healthy_life.entity.product;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_category_details")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCategoryDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "p_category_details_id")
    private Long pCategoryDetailsId;

    @OneToMany(mappedBy = "productCategoryDetail", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "p_category_id", nullable = false)
    private ProductCategory productCategory;

    @Column(name = "p_category_details_name", nullable = false)
    private String pCategoryDetailName;

}
