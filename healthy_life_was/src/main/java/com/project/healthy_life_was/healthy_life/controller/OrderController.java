package com.project.healthy_life_was.healthy_life.controller;

import com.project.healthy_life_was.healthy_life.common.constant.ApiMappingPattern;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.order.request.CartOrderRequestDto;
import com.project.healthy_life_was.healthy_life.dto.order.request.DirectOrderRequestDto;
import com.project.healthy_life_was.healthy_life.dto.order.request.OrderDetailIdListRequestDto;
import com.project.healthy_life_was.healthy_life.dto.order.response.*;
import com.project.healthy_life_was.healthy_life.dto.payment.CancelRequestDto;
import com.project.healthy_life_was.healthy_life.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.ORDER)
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final String ORDER_POST_CART = "/carts";
    private final String ORDER_POST_DIRECT = "/{pId}";
    private final String ORDER_STATUS_PUT = "/order-status";
    private final String ORDER_PUT_CANCEL = "/cancel/{orderDetailId}";
    private final String ORDER_GET_REVIEW = "/review-writable";
    private final String ORDER_POST_PAY_CANCEL = "/pay/cancel";

    @PostMapping(ORDER_POST_DIRECT)
    public ResponseEntity<ResponseDto<PostOrderResponseDto>> directOrder (
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long pId,
            @Valid @RequestBody DirectOrderRequestDto dto
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<PostOrderResponseDto> response = orderService.directOrder(username, pId, dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @PostMapping(ORDER_POST_CART)
    public ResponseEntity<ResponseDto<PostOrderResponseDto>> cartOrder (
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CartOrderRequestDto dto
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<PostOrderResponseDto> response = orderService.cartOrder(username, dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<OrderListResponseDto>> getOrder (
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startOrderDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endOrderDate
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<OrderListResponseDto> response = orderService.getOrder(username, startOrderDate, endOrderDate);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @PutMapping(ORDER_STATUS_PUT)
    public ResponseEntity<ResponseDto<OrderListResponseDto>> changeOrderStatus (
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody OrderDetailIdListRequestDto dto,
            @RequestParam String orderStatus
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<OrderListResponseDto> response = orderService.changeOrderStatus(username, dto, orderStatus);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @PutMapping(ORDER_PUT_CANCEL)
    public ResponseEntity<ResponseDto<OrderCancelResponseDto>> cancelReturnOrExchange (
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long orderDetailId
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<OrderCancelResponseDto> response = orderService.cancelReturnOrExchange(username, orderDetailId);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(ORDER_GET_REVIEW)
    public ResponseEntity<ResponseDto<OrderListResponseDto>> orderGetReview (
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<OrderListResponseDto> response = orderService.orderGetReview(username);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @PostMapping(ORDER_POST_PAY_CANCEL)
    public ResponseEntity<ResponseDto<List<OrderCancelResponseDto>>> orderCancel (
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CancelRequestDto dto
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<List<OrderCancelResponseDto>> response = orderService.orderCancel(username,dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

}
