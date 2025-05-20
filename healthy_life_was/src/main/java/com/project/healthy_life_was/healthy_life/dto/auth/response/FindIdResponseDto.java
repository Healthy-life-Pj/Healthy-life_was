package com.project.healthy_life_was.healthy_life.dto.auth.response;

import com.project.healthy_life_was.healthy_life.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindIdResponseDto {
    private String name;
    private String username;

    public FindIdResponseDto(User user) {
        this.name = user.getName();
        this.username = user.getUsername();
    }
}
