package com.project.healthy_life_was.healthy_life.service;

import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.wishList.response.WishlistCountResponseDto;
import com.project.healthy_life_was.healthy_life.dto.wishList.response.WishlistResponseDto;

public interface WishListService {
    ResponseDto<WishlistResponseDto> addProduct(String username, Long pId);
    ResponseDto<WishlistResponseDto> getMyWishList(String username);
    ResponseDto<WishlistCountResponseDto> getWishListCount(Long pId);
    ResponseDto<Void> resetWishList(String username);
    ResponseDto<WishlistResponseDto> deleteProductFromWishList(String username, Long pId);
}
