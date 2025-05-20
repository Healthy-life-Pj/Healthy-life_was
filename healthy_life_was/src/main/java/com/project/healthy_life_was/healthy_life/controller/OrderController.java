package com.project.healthy_life_was.healthy_life.controller;

import com.project.healthy_life_was.healthy_life.common.constant.ApiMappingPattern;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.order.request.CartOrderRequestDto;
import com.project.healthy_life_was.healthy_life.dto.order.request.DirectOrderRequestDto;
import com.project.healthy_life_was.healthy_life.dto.order.request.OrderGetRequestDto;
import com.project.healthy_life_was.healthy_life.dto.order.response.CartOrderResponseDto;
import com.project.healthy_life_was.healthy_life.dto.order.response.DirectOrderResponseDto;
import com.project.healthy_life_was.healthy_life.dto.order.response.OrderCancelResponseDto;
import com.project.healthy_life_was.healthy_life.dto.order.response.OrderDetailResponseDto;
import com.project.healthy_life_was.healthy_life.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiMappingPattern.ORDER)
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final String ORDER_POST_CART = "carts";
    private final String ORDER_POST_DIRECT = "/{pId}";
    private final String ORDER_PUT = "/{orderDetailId}";

    @PostMapping(ORDER_POST_DIRECT)
    public ResponseEntity<ResponseDto<DirectOrderResponseDto>> directOrder (
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long pId,
            @RequestBody DirectOrderRequestDto dto
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<DirectOrderResponseDto> response = orderService.directOrder(username, pId, dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @PostMapping(ORDER_POST_CART)
    public ResponseEntity<ResponseDto<CartOrderResponseDto>> cartOrder (
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CartOrderRequestDto dto
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<CartOrderResponseDto> response = orderService.cartOrder(username, dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<OrderDetailResponseDto>> getOrder (
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody OrderGetRequestDto dto
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<OrderDetailResponseDto> response = orderService.getOrder(username, dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @PutMapping(ORDER_PUT)
    public ResponseEntity<ResponseDto<OrderCancelResponseDto>> cancelOrder (
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long orderDetailId
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<OrderCancelResponseDto> response = orderService.cancelOrder(username, orderDetailId);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

}
