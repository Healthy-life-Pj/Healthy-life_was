package com.project.healthy_life_was.healthy_life.dto.review.response;

import com.project.healthy_life_was.healthy_life.entity.review.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewUpdateResponseDto {

    private Long reviewId;
    private String pName;
    private String username;
    private Double reviewRating;
    private String reviewContent;
    private String reviewImgUrl;
    private LocalDate reviewCreatAt;

    public ReviewUpdateResponseDto(Review review) {
        this.reviewId = review.getReviewId();
        this.pName = review.getOrderDetail().getProduct().getPName();
        this.reviewRating = review.getReviewRating();
        this.reviewContent = review.getReviewContent();
        this.reviewImgUrl = review.getReviewImgUrl();
        this.reviewCreatAt = review.getReviewCreatAt();
    }
}
