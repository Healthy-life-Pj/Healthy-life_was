package com.project.healthy_life_was.healthy_life.dto.wishList.response;

import com.project.healthy_life_was.healthy_life.entity.product.Product;
import com.project.healthy_life_was.healthy_life.entity.whishList.WishListItem;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WishListItemDto {
    private Long pId;
    private String pName;
    private int pPrice;
    private String pImgUrl;
    private LocalDateTime wishListAddedAt;

    public WishListItemDto (Product product, WishListItem wishListItem) {
        this.pId = product.getPId();
        this.pName = product.getPName();
        this.pPrice = product.getPPrice();
        this.pImgUrl = product.getPImgUrl();
        this.wishListAddedAt = wishListItem.getWishListAddedAt();
    }
}
