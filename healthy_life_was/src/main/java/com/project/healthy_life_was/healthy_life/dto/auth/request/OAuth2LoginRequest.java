package com.project.healthy_life_was.healthy_life.dto.auth.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2LoginRequest {
    private String email;
    private String name;
    private String snsId;
    private String provider;
}
