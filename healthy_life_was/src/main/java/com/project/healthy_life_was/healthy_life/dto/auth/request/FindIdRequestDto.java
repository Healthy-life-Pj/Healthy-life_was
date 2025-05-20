package com.project.healthy_life_was.healthy_life.dto.auth.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindIdRequestDto {

    private String name;

    private String userEmail;
}