package com.project.healthy_life_was.healthy_life.controller;

import com.project.healthy_life_was.healthy_life.common.constant.ApiMappingPattern;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.review.request.ReviewCreateRequestDto;
import com.project.healthy_life_was.healthy_life.dto.review.request.ReviewUpdateRequestDto;
import com.project.healthy_life_was.healthy_life.dto.review.response.ProductReviewListResponseDto;
import com.project.healthy_life_was.healthy_life.dto.review.response.ReviewCreateResponseDto;
import com.project.healthy_life_was.healthy_life.dto.review.response.ReviewUpdateResponseDto;
import com.project.healthy_life_was.healthy_life.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.REVIEW)
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;


    private final String REVIEW_POST = "/{orderDetailId}";
    private final String REVIEW_MINE = "/me";
    private final String REVIEW_PUT = "/{reviewId}";
    private final String REVIEW_DELETE = "/{reviewId}";
    private final String REVIEW_EXISTS = "/{orderDetailId}/duplication";

    @PostMapping(REVIEW_POST)
    public ResponseEntity<ResponseDto<ReviewCreateResponseDto>> createReview (
            @AuthenticationPrincipal UserDetails  userDetails,
            @PathVariable Long orderDetailId,
            @RequestBody ReviewCreateRequestDto dto
            ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<ReviewCreateResponseDto> response = reviewService.createReview(username, orderDetailId, dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK: HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(REVIEW_MINE)
    public ResponseEntity<ResponseDto<ProductReviewListResponseDto>> getMyReview (@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<ProductReviewListResponseDto> response = reviewService.getMyReview(username);
        HttpStatus status = response.isResult() ? HttpStatus.OK: HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @PutMapping(REVIEW_PUT)
    public ResponseEntity<ResponseDto<ReviewUpdateResponseDto>> updateReview (
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long reviewId,
            @RequestBody ReviewUpdateRequestDto dto
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<ReviewUpdateResponseDto> response = reviewService.updateReview(username, reviewId, dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK: HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @DeleteMapping(REVIEW_DELETE)
    public ResponseEntity<ResponseDto<Void>> deleteReview (
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long reviewId
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<Void> response = reviewService.deleteReview(username, reviewId);
        HttpStatus status = response.isResult() ? HttpStatus.OK: HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(REVIEW_EXISTS)
    public ResponseEntity<ResponseDto<Boolean>> duplicateReview (
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long orderDetailId
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<Boolean> response = reviewService.duplicateReview(username, orderDetailId);
        HttpStatus status = response.isResult() ? HttpStatus.OK: HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

}
