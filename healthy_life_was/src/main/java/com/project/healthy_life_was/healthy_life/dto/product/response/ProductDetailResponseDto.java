package com.project.healthy_life_was.healthy_life.dto.product.response;

import com.project.healthy_life_was.healthy_life.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponseDto {

    private Long pId;
    private String pName;
    private int pPrice;
    private String pDescription;
    private String pIngredients;
    private String pNutritionInfo;
    private String pOrigin;
    private String pUsage;
    private Date pExpirationDate;
    private String pManufacturer;
    private String pImgUrl;
    private int averageRating;

    public ProductDetailResponseDto(Product product, double averageRating) {

        this.pId = product.getPId();
        this.pName = product.getPName();
        this.pPrice = product.getPPrice();
        this.pDescription = product.getPDescription();
        this.pIngredients = product.getPIngredients();
        this.pNutritionInfo = product.getPNutritionInfo();
        this.pOrigin = product.getPOrigin();
        this.pUsage = product.getPUsage();
        this.pExpirationDate = product.getPExpirationDate();
        this.pManufacturer = product.getPManufacturer();
        this.pImgUrl = product.getPImgUrl();
        this.averageRating =(int) averageRating;

    }
}
