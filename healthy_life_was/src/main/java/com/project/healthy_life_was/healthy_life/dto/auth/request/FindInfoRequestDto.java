package com.project.healthy_life_was.healthy_life.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindInfoRequestDto {

    @NotBlank(message = "Email cannot be blank")
    private String email;

    private String username;
}