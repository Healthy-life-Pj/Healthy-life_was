package com.project.healthy_life_was.healthy_life.dto.auth.request;

import com.project.healthy_life_was.healthy_life.entity.user.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank(message = "Confirm Password cannot be blank")
    private String confirmPassword;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "User Nickname cannot be blank")
    private String userNickName;

    @Past(message = "Birthdate must be in the past")
    private Date userBirth;

    @NotBlank(message = "Email cannot be blank")
    private String userEmail;

    @NotBlank(message = "Phone number cannot be blank")
    private String userPhone;

    @NotBlank(message = "Address cannot be blank")
    private String address;

    @NotBlank(message = "Address detail cannot be blank")
    private String addressDetail;

    @NotNull(message = "post num cannot be null")
    private int postNum;

    @NotNull(message = "Gender cannot be null")
    private Gender userGender;

    @NotBlank
    @Pattern(regexp="^(home|kakao|naver)$")
    private String joinPath;
    private String snsId;
}
