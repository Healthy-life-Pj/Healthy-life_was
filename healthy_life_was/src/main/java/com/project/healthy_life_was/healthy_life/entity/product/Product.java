package com.project.healthy_life_was.healthy_life.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.healthy_life_was.healthy_life.entity.physique.PhysiqueTag;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "p_id", nullable = false, updatable = false)
    private Long pId;

    @Column(name = "p_name", nullable = false)
    private String pName;

    @Column(name = "p_price", nullable = false)
    private int pPrice;

    @Column(name = "p_description", nullable = false)
    private String pDescription;

    @Column(name = "p_ingredients", nullable = false)
    private String pIngredients;

    @Column(name = "p_nutrition_info", nullable = false)
    private String pNutritionInfo;

    @Column(name = "p_origin", nullable = false)
    private String pOrigin;

    @Column(name = "p_usage", nullable = false)
    private String pUsage;

    @Column(name = "p_expiration_date", nullable = false)
    private Date pExpirationDate;

    @Column(name = "p_manufacturer", nullable = false)
    private String pManufacturer;

    @Column(name = "p_img_url", nullable = false)
    private String pImgUrl;

    @Column(name = "p_stock_status", nullable = false)
    private int pStockStatus;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PhysiqueTag> physiqueTags = new ArrayList<>();

}
