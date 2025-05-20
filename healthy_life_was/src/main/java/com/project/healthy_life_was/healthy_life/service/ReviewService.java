package com.project.healthy_life_was.healthy_life.service;

import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.review.request.ReviewCreateRequestDto;
import com.project.healthy_life_was.healthy_life.dto.review.request.ReviewUpdateRequestDto;
import com.project.healthy_life_was.healthy_life.dto.review.response.ProductReviewListResponseDto;
import com.project.healthy_life_was.healthy_life.dto.review.response.ReviewCreateResponseDto;
import com.project.healthy_life_was.healthy_life.dto.review.response.ReviewUpdateResponseDto;

import java.util.List;

public interface ReviewService {
    ResponseDto<ReviewCreateResponseDto> createReview(String username, Long orderDetailId, ReviewCreateRequestDto dto);

    ResponseDto<ProductReviewListResponseDto> getMyReview(String username);

    ResponseDto<ReviewUpdateResponseDto> updateReview(String username, Long reviewId, ReviewUpdateRequestDto dto);

    ResponseDto<Void> deleteReview(String username, Long reviewId);

    ResponseDto<Boolean> duplicateReview(String username, Long orderDetailId);

    ResponseDto<ProductReviewListResponseDto> getAllReview();

    ResponseDto<ProductReviewListResponseDto> getAllReviewProduct(Long pId);
}
