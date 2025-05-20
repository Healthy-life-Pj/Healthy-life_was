package com.project.healthy_life_was.healthy_life.dto.wishList.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistCountResponseDto {
    private Long pId;
    private int pRecommendCount;
}
