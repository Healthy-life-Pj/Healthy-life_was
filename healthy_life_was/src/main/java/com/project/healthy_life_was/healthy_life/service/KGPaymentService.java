package com.project.healthy_life_was.healthy_life.service;

import com.project.healthy_life_was.healthy_life.dto.payment.ApiResponseDto;
import com.project.healthy_life_was.healthy_life.dto.payment.CancelRequestDto;
import com.project.healthy_life_was.healthy_life.dto.payment.VerifyRequestDto;

public interface KGPaymentService {
    ApiResponseDto verify(VerifyRequestDto dto);

    ApiResponseDto cancel(CancelRequestDto dto);
}
