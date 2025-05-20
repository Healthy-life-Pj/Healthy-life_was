package com.project.healthy_life_was.healthy_life.controller;

import com.project.healthy_life_was.healthy_life.common.constant.ApiMappingPattern;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.shipping.response.ShippingResponseDto;
import com.project.healthy_life_was.healthy_life.security.PrincipalUser;
import com.project.healthy_life_was.healthy_life.service.ShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ShippingController {
    private final ShippingService shippingService;

    private final String TRACKING_INFO = ApiMappingPattern.SHIPPING + "/tracking/{trackingNum}";
    private final String ORDER_SHIPPING = ApiMappingPattern.ORDER + "/{orderId}/shipping";
    private final String USER_SHIPPING = ApiMappingPattern.USER + "/me/shipping";

    @PostMapping(ORDER_SHIPPING)
    public ResponseEntity<ResponseDto<ShippingResponseDto>> createShipping (
            @AuthenticationPrincipal PrincipalUser principalUser,
            @PathVariable Long orderId
            ){
        ResponseDto<ShippingResponseDto> response = shippingService.createShipping(orderId);
        HttpStatus status = response.isResult()? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(TRACKING_INFO)
    public ResponseEntity<ResponseDto<ShippingResponseDto>> findShippingByTrackingNumber (
            @AuthenticationPrincipal PrincipalUser principalUser,
            @PathVariable int trackingNum
    ){
        ResponseDto<ShippingResponseDto> response = shippingService.findShippingByTrackingNumber(trackingNum);
        HttpStatus status = response.isResult()? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(USER_SHIPPING)
    public ResponseEntity<ResponseDto<List<ShippingResponseDto>>> getUserShipping (
            @AuthenticationPrincipal PrincipalUser principalUser
    ){
        String username = principalUser.getUsername();
        ResponseDto<List<ShippingResponseDto>> response = shippingService.getUserShipping(username);
        HttpStatus status = response.isResult()? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }


    @GetMapping(ORDER_SHIPPING)
    public ResponseEntity<ResponseDto<ShippingResponseDto>> findShippingByOrderId (
            @AuthenticationPrincipal PrincipalUser principalUser,
            @PathVariable Long orderId
    ){
        String username = principalUser.getUsername();
        ResponseDto<ShippingResponseDto> response = shippingService.findShippingByOrderId(username, orderId);
        HttpStatus status = response.isResult()? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }
}