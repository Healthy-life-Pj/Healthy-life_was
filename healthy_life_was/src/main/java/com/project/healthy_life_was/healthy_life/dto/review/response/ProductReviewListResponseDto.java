package com.project.healthy_life_was.healthy_life.dto.review.response;

import com.project.healthy_life_was.healthy_life.dto.review.ReviewListDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewListResponseDto {
    private List<ReviewListDto> reviewListDto;

}
