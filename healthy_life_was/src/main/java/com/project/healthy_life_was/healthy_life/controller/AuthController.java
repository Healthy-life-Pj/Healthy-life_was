package com.project.healthy_life_was.healthy_life.controller;


import com.project.healthy_life_was.healthy_life.common.constant.ApiMappingPattern;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.auth.request.FindInfoRequestDto;
import com.project.healthy_life_was.healthy_life.dto.auth.request.LoginRequestDto;
import com.project.healthy_life_was.healthy_life.dto.auth.request.SignUpRequestDto;
import com.project.healthy_life_was.healthy_life.dto.auth.response.FindInfoResponseDto;
import com.project.healthy_life_was.healthy_life.dto.auth.response.LoginResponseDto;
import com.project.healthy_life_was.healthy_life.dto.auth.response.SignUpResponseDto;
import com.project.healthy_life_was.healthy_life.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiMappingPattern.AUTH)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final String LOGIN = "/login";
    private final String SIGN_UP = "/sign-up";
    private final String DUPLICATE_USERNAME = "/duplicate/username/{username}";
    private final String DUPLICATE_USER_NICKNAME = "/duplicate/userNickName/{userNickName}";
    private final String SNS_LOGIN = "/sns-login";
    private final String SNS_SIGN_UP = "/sns-sign-up";
    private final String RECOVERY_EMAIL = "/recovery-email";

    @PostMapping(SIGN_UP)
    public ResponseEntity<ResponseDto<SignUpResponseDto>> signUp (@Valid @RequestBody SignUpRequestDto dto) {
        ResponseDto<SignUpResponseDto> response = authService.signUp(dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @PostMapping(LOGIN)
    public ResponseEntity<ResponseDto<LoginResponseDto>> login (@Valid @RequestBody LoginRequestDto dto) {
        ResponseDto<LoginResponseDto> response = authService.login(dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(DUPLICATE_USERNAME)
    public ResponseEntity<ResponseDto<Boolean>> duplicateUserName (@PathVariable String username) {
        ResponseDto<Boolean> response = authService.duplicateUserName(username);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(DUPLICATE_USER_NICKNAME)
    public ResponseEntity<ResponseDto<Boolean>> duplicateUserNickName (@PathVariable String userNickName) {
        ResponseDto<Boolean> response = authService.duplicateUserNickName(userNickName);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @PostMapping(RECOVERY_EMAIL)
    public ResponseEntity<ResponseDto<FindInfoResponseDto>> recoveryEmail (
            @RequestBody FindInfoRequestDto dto
    ) throws MessagingException {
        ResponseDto<FindInfoResponseDto> response = authService.recoveryEmail(dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }
}
