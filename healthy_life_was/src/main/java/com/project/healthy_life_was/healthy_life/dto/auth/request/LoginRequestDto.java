package com.project.healthy_life_was.healthy_life.dto.auth.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    @NotNull
    private String username;

    @NotNull
    private String password;
}
