package com.project.healthy_life_was.healthy_life.dto.cart.response;

import com.project.healthy_life_was.healthy_life.entity.cart.Cart;
import com.project.healthy_life_was.healthy_life.entity.cart.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartAddResponseDto {
    private Long cartId;

    private Long pId;

    private String pName;

    private int productQuantity;

    private int productPrice;

    private String pImgUrl;


    public CartAddResponseDto(CartItem cartItem) {
        this.cartId = cartItem.getCart().getCartId();
        this.pId = cartItem.getProduct().getPId();
        this.pName = cartItem.getProduct().getPName();
        this.productQuantity = cartItem.getProductQuantity();
        this.productPrice = cartItem.getProductPrice();
        this.pImgUrl = cartItem.getProduct().getPImgUrl();
    }
}
