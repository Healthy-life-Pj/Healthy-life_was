package com.project.healthy_life_was.healthy_life.dto.cart.response;

import com.project.healthy_life_was.healthy_life.entity.cart.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartUpdateResponseDto {
    private Long cartId;

    private Long cartItemId;

    private String username;

    private Long pId;

    private String pImgUrl;

    private String pName;

    private int productQuantity;

    private int productPrice;

    public CartUpdateResponseDto(CartItem cartItem) {
        this.cartId = cartItem.getCart().getCartId();
        this.cartItemId = cartItem.getCartItemId();
        this.pId = cartItem.getProduct().getPId();
        this.pName = cartItem.getProduct().getPName();
        this.username = cartItem.getCart().getUser().getUsername();
        this.productQuantity = cartItem.getProductQuantity();
        this.productPrice = cartItem.getProductPrice();
        this.pImgUrl = cartItem.getProduct().getPImgUrl();
    }
}
