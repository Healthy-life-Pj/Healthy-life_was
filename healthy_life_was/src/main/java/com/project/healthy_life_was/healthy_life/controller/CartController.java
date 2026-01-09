package com.project.healthy_life_was.healthy_life.controller;

import com.project.healthy_life_was.healthy_life.common.constant.ApiMappingPattern;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.cart.request.*;
import com.project.healthy_life_was.healthy_life.dto.cart.response.CartAddResponseDto;

import com.project.healthy_life_was.healthy_life.dto.cart.response.CartDetailResponseDto;
import com.project.healthy_life_was.healthy_life.dto.cart.response.CartUpdateResponseDto;
import com.project.healthy_life_was.healthy_life.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.CART)
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    private final String CART_POST = "/products/{pId}";
    private final String CART_GET = "/me";
    private final String CART_SELECT_GET = "/some/cartItemIds";
    private final String CART_PUT = "/products/{cartItemId}/quantity";
    private final String CART_DELETE_PRODUCT = "/cartItemIds";
    private final String CART_DELETE_ALL = "/products/all";
    private final String CART_ITEM_LIST_GET = "/search/cartItems";

    @PostMapping(CART_POST)
    public ResponseEntity<ResponseDto<CartAddResponseDto>> createCart (
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long pId,
            @RequestBody CartAddRequestDto dto
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<CartAddResponseDto> response = cartService.createCart(username, pId, dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(CART_GET)
    public ResponseEntity<ResponseDto<CartDetailResponseDto>> getCartUser (
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<CartDetailResponseDto> response = cartService.getCartUser(username);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(CART_SELECT_GET)
    public ResponseEntity<ResponseDto<CartDetailResponseDto>> getCartSelect (
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CartSelectRequestDto dto
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<CartDetailResponseDto> response = cartService.getCartSelect(username, dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @PutMapping(CART_PUT)
    public ResponseEntity<ResponseDto<CartUpdateResponseDto>> updateCart (
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long cartItemId,
            @RequestBody CartUpdateQuantityRequestDto dto
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<CartUpdateResponseDto> response = cartService.updateCart(username, cartItemId, dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @DeleteMapping(CART_DELETE_PRODUCT)
    public ResponseEntity<Object> deleteCartItemIds (
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody DeleteCartItemsDto dto
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<Object> response = cartService.deleteCartItemIds(username, dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @DeleteMapping(CART_DELETE_ALL)
    public ResponseEntity<Object> deleteCartAll (
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<Object> response = cartService.deleteCartAll(username);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(CART_ITEM_LIST_GET)
    public ResponseEntity<ResponseDto<CartDetailResponseDto>> getCartItemList (
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam List<Long> cartItemIds
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<CartDetailResponseDto> response = cartService.getCartItemList(username, cartItemIds);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }
}
