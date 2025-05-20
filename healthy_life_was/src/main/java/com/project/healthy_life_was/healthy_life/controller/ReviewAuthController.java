package com.project.healthy_life_was.healthy_life.controller;

import com.project.healthy_life_was.healthy_life.common.constant.ApiMappingPattern;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.review.response.ProductReviewListResponseDto;
import com.project.healthy_life_was.healthy_life.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiMappingPattern.AUTH)
@RequiredArgsConstructor
public class ReviewAuthController {

    private final ReviewService reviewService;

    private final String REVIEW_ALL = "/reviews/all";
    private final String REVIEW_PRODUCT = "/reviews/products/{pId}";

    @GetMapping(REVIEW_ALL)
    public ResponseEntity<ResponseDto<ProductReviewListResponseDto>> getAllReview () {
        ResponseDto<ProductReviewListResponseDto> response = reviewService.getAllReview();
        HttpStatus status = response.isResult() ? HttpStatus.OK: HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(REVIEW_PRODUCT)
    public ResponseEntity<ResponseDto<ProductReviewListResponseDto>> getAllReviewProduct (@PathVariable Long pId) {
        ResponseDto<ProductReviewListResponseDto> response = reviewService.getAllReviewProduct(pId);
        HttpStatus status = response.isResult() ? HttpStatus.OK: HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

}
