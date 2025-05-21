package com.project.healthy_life_was.healthy_life.dto.product.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.healthy_life_was.healthy_life.entity.physique.PhysiqueTag;
import com.project.healthy_life_was.healthy_life.entity.product.Product;
import com.project.healthy_life_was.healthy_life.entity.product.ProductCategoryDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductListResponseDto {
    @JsonProperty("pId")
    private Long pId;
    @JsonProperty("pName")
    private String pName;
    @JsonProperty("pPrice")
    private int pPrice;
    private String pImgUrl;
    @JsonProperty("pCategoryName")
    private String pCategoryName;
    @JsonProperty("pCategoryDetailName")
    private String pCategoryDetailName;
    private List<String> physiqueName;
    private int averageRating;

    public ProductListResponseDto(Product product, double averageRating, ProductCategoryDetail productCategoryDetail) {
        this.pId = product.getPId();
        this.pName = product.getPName();
        this.pPrice = product.getPPrice();
        this.pImgUrl = product.getPImgUrl();
        this.pCategoryName = productCategoryDetail.getProductCategory().getPCategoryName();
        this.pCategoryDetailName = productCategoryDetail.getPCategoryDetailName();
        this.physiqueName = product.getPhysiqueTags().stream()
                .map(PhysiqueTag::getPhysiqueName)
                .toList();
        this.averageRating = (int) averageRating;
    }
}
