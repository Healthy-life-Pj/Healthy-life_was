package com.project.healthy_life_was.healthy_life.dto.review.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateRequestDto {

    @NotNull
    private Double reviewRating;
    @NotNull
    private String reviewContent;

    private MultipartFile reviewImgUrl;

}
