package com.project.healthy_life_was.healthy_life.entity.product;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "p_id", referencedColumnName = "p_id", nullable = false)
//    @JsonIgnore
    private Product product;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "p_category_id", nullable = false)
//    @JsonIgnore
    private ProductCategory productCategory;

    @Column(name = "p_category_details_name", nullable = false)
    private String pCategoryDetailName;

}
