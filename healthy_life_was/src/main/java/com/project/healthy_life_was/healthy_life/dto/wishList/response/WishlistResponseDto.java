package com.project.healthy_life_was.healthy_life.dto.wishList.response;

import com.project.healthy_life_was.healthy_life.entity.whishList.WishList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class WishlistResponseDto {
    private Long wishListId;
    private List<WishListItemDto> wishListItems;

    public WishlistResponseDto (WishList wishList, List<WishListItemDto> wishListItems) {
        this.wishListId = wishList.getWishListId();
        this.wishListItems = wishListItems;
    }
}
