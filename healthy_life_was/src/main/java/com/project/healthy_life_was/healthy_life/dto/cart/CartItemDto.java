package com.project.healthy_life_was.healthy_life.dto.cart;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItemDto {
    @JsonProperty("cartItemId")
    private Long cartItemId;
    @JsonProperty("pId")
    private Long pId;
    @JsonProperty("pName")
    private String pName;
    @JsonProperty("productQuantity")
    private int productQuantity;
    @JsonProperty("productPrice")
    private int productPrice;
    @JsonProperty("pImgUrl")
    private String pImgUrl;

}
