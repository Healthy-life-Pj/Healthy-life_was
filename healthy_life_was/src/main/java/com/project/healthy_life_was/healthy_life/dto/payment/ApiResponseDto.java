package com.project.healthy_life_was.healthy_life.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponseDto {
    private String status;
    private String message;
    private Map<String, Object> data;

    public static ApiResponseDto ok(Map<String, Object> d) {
        return ApiResponseDto.builder()
                .status("OK")
                .data(d)
                .build();
    }

    public static ApiResponseDto fail(String msg) {
        return ApiResponseDto.builder()
                .status("FAIL")
                .message(msg)
                .build();
    }}
