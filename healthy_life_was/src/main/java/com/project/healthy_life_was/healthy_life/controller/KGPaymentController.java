package com.project.healthy_life_was.healthy_life.controller;

import com.project.healthy_life_was.healthy_life.dto.payment.ApiResponseDto;
import com.project.healthy_life_was.healthy_life.dto.payment.CancelRequestDto;
import com.project.healthy_life_was.healthy_life.dto.payment.VerifyRequestDto;
import com.project.healthy_life_was.healthy_life.service.KGPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments/iamport")
@RequiredArgsConstructor
public class KGPaymentController {
    private final KGPaymentService kgPaymentService;

    private final String VERIFY_POST = "/verify";
    private final String CANCEL_POST = "/cancel";

    @PostMapping(VERIFY_POST)
    public ResponseEntity<ApiResponseDto> verify (
            @RequestBody VerifyRequestDto dto
    ) {
        return ResponseEntity.ok(kgPaymentService.verify(dto));
    }

    @PostMapping(CANCEL_POST)
    public ResponseEntity<ApiResponseDto> cancel (
            @RequestBody CancelRequestDto dto
    ) {
        return ResponseEntity.ok(kgPaymentService.cancel(dto));
    }
}
