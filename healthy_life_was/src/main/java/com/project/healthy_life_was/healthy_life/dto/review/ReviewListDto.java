package com.project.healthy_life_was.healthy_life.dto.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewListDto {
    private Long reviewId;
    @JsonProperty("pName")
    private String pName;
    private String pImgUrl;
    private String username;
    private Double reviewRating;
    private String reviewContent;
    private String reviewImgUrl;
    private LocalDate reviewCreatAt;
}
