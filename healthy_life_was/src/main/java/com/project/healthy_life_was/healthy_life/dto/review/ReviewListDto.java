package com.project.healthy_life_was.healthy_life.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewListDto {
    private Long reviewId;
    private String pName;
    private String username;
    private Double reviewRating;
    private String reviewContent;
    private String reviewImgUrl;
    private LocalDate reviewCreatAt;

}
