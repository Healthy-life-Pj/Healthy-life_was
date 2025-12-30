package com.project.healthy_life_was.healthy_life.dto.product;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrawledProductDto {
    private String name;
    private int price;
    private String description;
    private String imageUrl;
    private String ingredients;
    private String nutrition;
    private String origin;
}
