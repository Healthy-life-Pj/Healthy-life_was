package com.project.healthy_life_was.healthy_life.dto.user.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PasswordUpdateRequestDto {

    @NotBlank(message = "현재 비밀번호를 입력해주세요")
    private String currentPassword;

    @NotBlank(message = "새 비밀번호를 입력해주세요")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 모두 포함해야 합니다"
    )
    private String userPassword;

    @NotBlank(message = "비밀번호 확인을 입력해주세요")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 모두 포함해야 합니다"
    )
    private String confirmUserPassword;

    @AssertTrue(message = "새 비밀번호가 일치하지 않습니다")
    public boolean isPasswordMatching() {
        return userPassword != null && userPassword.equals(confirmUserPassword);
    }
}
