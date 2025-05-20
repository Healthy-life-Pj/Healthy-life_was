package com.project.healthy_life_was.healthy_life.controller;

import com.project.healthy_life_was.healthy_life.common.constant.ApiMappingPattern;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.wishList.response.WishlistCountResponseDto;
import com.project.healthy_life_was.healthy_life.dto.wishList.response.WishlistResponseDto;
import com.project.healthy_life_was.healthy_life.security.PrincipalUser;
import com.project.healthy_life_was.healthy_life.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiMappingPattern.WISH_LIST)
@RequiredArgsConstructor
public class WishListController {
    private final WishListService wishListService;

    private final String WISHLIST_PRODUCT = "/products/{pId}";
    private final String MY_WISHLIST = "/me";
    private final String COUNT_OF_WISHlIST = "/count/products/{pId}";

    @PostMapping(WISHLIST_PRODUCT)
    private ResponseEntity<ResponseDto<WishlistResponseDto>> addProduct (
            @AuthenticationPrincipal PrincipalUser principalUser,
            @PathVariable Long pId
    ){
        String username = principalUser.getUsername();
        ResponseDto<WishlistResponseDto> response = wishListService.addProduct(username, pId);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(MY_WISHLIST)
    private ResponseEntity<ResponseDto<WishlistResponseDto>> getMyWishList (
            @AuthenticationPrincipal PrincipalUser principalUser
            ){
        String username = principalUser.getUsername();
        ResponseDto<WishlistResponseDto> response = wishListService.getMyWishList(username);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(COUNT_OF_WISHlIST)
    private ResponseEntity<ResponseDto<WishlistCountResponseDto>> getWishListCount (
            @PathVariable Long pId
            ){
        ResponseDto<WishlistCountResponseDto> response = wishListService.getWishListCount(pId);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @PutMapping(MY_WISHLIST)
    private ResponseEntity<ResponseDto<Void>> resetWishList (
            @AuthenticationPrincipal PrincipalUser principalUser
    ) {
        String username = principalUser.getUsername();
        ResponseDto<Void> response = wishListService.resetWishList(username);
        HttpStatus status = response.isResult() ? HttpStatus.NO_CONTENT : HttpStatus.FORBIDDEN;
        return ResponseEntity.status(status).body(response);
    }

    @DeleteMapping(WISHLIST_PRODUCT)
    private ResponseEntity<ResponseDto<WishlistResponseDto>> deleteProductFromWishList (
            @AuthenticationPrincipal PrincipalUser principalUser,
            @PathVariable Long pId
    ) {
        String username = principalUser.getUsername();
        ResponseDto<WishlistResponseDto> response = wishListService.deleteProductFromWishList(username, pId);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }
}
