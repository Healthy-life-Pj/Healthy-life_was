package com.project.healthy_life_was.healthy_life.dto.user.request;

import com.project.healthy_life_was.healthy_life.common.constant.ResponseMessage;
import com.project.healthy_life_was.healthy_life.entity.user.Gender;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class UserUpdateRequestDto {

    @Pattern(
            regexp = "^[가-힣a-zA-Z]{2,10}$",
            message = ResponseMessage.VALIDATION_FAIL + "name"
    )
    private String name;

    @Pattern(
            regexp = "^[가-힣a-zA-Z\\d]{3,10}$",
            message = ResponseMessage.VALIDATION_FAIL + "userNickName"
    )
    private String userNickName;

    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = ResponseMessage.VALIDATION_FAIL + "userEmail"
    )
    private String userEmail;

    @Pattern(
            regexp = "^01[016789]\\d{7,8}$",
            message = ResponseMessage.VALIDATION_FAIL + "userPhone"
    )
    private String userPhone;

    private Date userBirth;

    private Gender userGender;
}