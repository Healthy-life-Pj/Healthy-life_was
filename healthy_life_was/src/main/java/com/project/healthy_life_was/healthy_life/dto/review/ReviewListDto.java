package com.project.healthy_life_was.healthy_life.dto.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.healthy_life_was.healthy_life.entity.review.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewListDto {
    private Long reviewId;
    @JsonProperty("pName")
    private String pName;
    @JsonProperty("pId")
    private Long pId;
    private String userNickName;
    private Double reviewRating;
    private String reviewContent;
    private String reviewImgUrl;
    private LocalDate reviewCreatAt;
    private LocalDateTime orderDate;

    public ReviewListDto(Review review) {
        this.reviewId = review.getReviewId();
        this.pName = review.getOrderDetail().getProduct().getPName();
        this.pId = review.getOrderDetail().getProduct().getPId();
        this.userNickName = review.getUser().getUserNickName();
        this.reviewRating = review.getReviewRating();
        this.reviewContent = review.getReviewContent();
        this.reviewImgUrl = review.getReviewImgUrl();
        this.reviewCreatAt = review.getReviewCreatAt();
        this.orderDate = review.getOrderDetail().getOrder().getOrderDate();
    }

}
