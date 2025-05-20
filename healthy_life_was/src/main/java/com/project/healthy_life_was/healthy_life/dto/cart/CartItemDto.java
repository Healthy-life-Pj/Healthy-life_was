package com.project.healthy_life_was.healthy_life.dto.cart;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItemDto {

    private Long cartItemId;
    private Long pId;
    private String pName;
    private int productQuantity;
    private int productPrice;
    private String pImgUrl;

}
