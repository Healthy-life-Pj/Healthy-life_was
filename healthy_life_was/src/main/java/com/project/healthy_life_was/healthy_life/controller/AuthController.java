package com.project.healthy_life_was.healthy_life.controller;

import com.project.healthy_life_was.healthy_life.common.constant.ApiMappingPattern;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.auth.request.FindInfoRequestDto;
import com.project.healthy_life_was.healthy_life.dto.auth.request.LoginRequestDto;
import com.project.healthy_life_was.healthy_life.dto.auth.request.OAuth2LoginRequest;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiMappingPattern.AUTH)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final String LOGIN = "/login";
    private final String SIGN_UP = "/sign-up";
    private final String DUPLICATE_USERNAME = "/duplicate/{username}";
    private final String DUPLICATE_USER_NICKNAME = "/duplicate/{userNickName}";
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
    public ResponseEntity<ResponseDto<Boolean>> duplicateUserName (@Valid @PathVariable String username) {
        ResponseDto<Boolean> response = authService.duplicateUserName(username);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(DUPLICATE_USER_NICKNAME)
    public ResponseEntity<ResponseDto<Boolean>> duplicateUserNickName (@Valid @PathVariable String userNickName) {
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

    @PostMapping("/oauth/{provider}")
    public ResponseEntity<ResponseDto<LoginResponseDto>> oauthLogin(
            @PathVariable String provider,
            @RequestBody OAuth2LoginRequest request) {

        // provider 검증
        if (!isValidProvider(provider)) {
            ResponseDto<LoginResponseDto> response = ResponseDto.setFailed("지원하지 않는 OAuth 제공업체입니다: " + provider);
            return ResponseEntity.badRequest().body(response);
        }

        ResponseDto<LoginResponseDto> response = authService.oauthLogin(
                request.getEmail(),
                request.getName(),
                request.getSnsId(),
                provider.toUpperCase()
        );

        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    private boolean isValidProvider(String provider) {
        return provider.equalsIgnoreCase("google") ||
                provider.equalsIgnoreCase("kakao") ||
                provider.equalsIgnoreCase("naver");
    }
}