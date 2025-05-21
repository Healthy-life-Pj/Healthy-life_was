package com.project.healthy_life_was.healthy_life.dto.product.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.healthy_life_was.healthy_life.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponseDto {

    @JsonProperty("pId")
    private Long pId;
    @JsonProperty("pName")
    private String pName;
    @JsonProperty("pPrice")
    private int pPrice;
    @JsonProperty("pDescription")
    private String pDescription;
    @JsonProperty("pIngredients")
    private String pIngredients;
    @JsonProperty("pNutritionInfo")
    private String pNutritionInfo;
    @JsonProperty("pOrigin")
    private String pOrigin;
    @JsonProperty("pUsage")
    private String pUsage;
    @JsonProperty("pExpirationDate")
    private Date pExpirationDate;
    @JsonProperty("pManufacturer")
    private String pManufacturer;
    @JsonProperty("pImgUrl")
    private String pImgUrl;
    @JsonProperty("averageRating")
    private int averageRating;
    @JsonProperty("pStockStatus")
    private int pStockStatus;

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
        this.pStockStatus = product.getPStockStatus();
        this.averageRating =(int) averageRating;

    }
}
