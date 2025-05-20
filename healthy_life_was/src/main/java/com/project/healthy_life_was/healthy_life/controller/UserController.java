package com.project.healthy_life_was.healthy_life.controller;

import com.project.healthy_life_was.healthy_life.common.constant.ApiMappingPattern;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.user.request.PasswordUpdateRequestDto;
import com.project.healthy_life_was.healthy_life.dto.user.request.UserDeleteRequestDto;
import com.project.healthy_life_was.healthy_life.dto.user.request.UserUpdateRequestDto;
import com.project.healthy_life_was.healthy_life.dto.user.response.UserInfoResponseDto;
import com.project.healthy_life_was.healthy_life.security.PrincipalUser;
import com.project.healthy_life_was.healthy_life.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiMappingPattern.USER)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final String GET_USER = "/me";
    private final String UPDATE_USER = "/me";
    private final String UPDATE_PASSWORD = "/me/password";
    private final String UPDATE_PASSWORD_BY_EMAIL = "/me/password/email";

    @GetMapping(GET_USER)
    private ResponseEntity<ResponseDto<UserInfoResponseDto>> getUserInfo (
            @AuthenticationPrincipal PrincipalUser principalUser
    ){
        String username = principalUser.getUsername();
        ResponseDto<UserInfoResponseDto> response = userService.getUserInfo(username);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @PutMapping(UPDATE_USER)
    private ResponseEntity<ResponseDto<UserInfoResponseDto>> updateUserInfo (
            @AuthenticationPrincipal PrincipalUser principalUser,
            @RequestBody @Valid UserUpdateRequestDto dto
    ){
        String username = principalUser.getUsername();
        ResponseDto<UserInfoResponseDto> response = userService.updateUserInfo(username, dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @PutMapping(UPDATE_PASSWORD)
    private ResponseEntity<ResponseDto<Void>> updatePwByMyPage (
            @AuthenticationPrincipal PrincipalUser principalUser,
            @RequestBody @Valid PasswordUpdateRequestDto dto
    ){
        String username = principalUser.getUsername();
        ResponseDto<Void> response = userService.updatePwByMyPage(username, dto);
        HttpStatus status = response.isResult() ? HttpStatus.NO_CONTENT : HttpStatus.FORBIDDEN;
        return ResponseEntity.status(status).body(response);
    }

    @PutMapping(UPDATE_PASSWORD_BY_EMAIL)
    private ResponseEntity<ResponseDto<Void>> updatePwByEmailToken (
            @RequestBody PasswordUpdateRequestDto dto,
            @RequestParam @Valid String token
    ) {
        ResponseDto<Void> response = userService.updatePwByEmailToken(token, dto);
        HttpStatus status = response.isResult() ? HttpStatus.NO_CONTENT : HttpStatus.FORBIDDEN;
        return ResponseEntity.status(status).body(response);
    }

    @DeleteMapping
    private ResponseEntity<ResponseDto<Void>> deleteUser (
            @AuthenticationPrincipal PrincipalUser principalUser,
            @RequestBody @Valid UserDeleteRequestDto dto
    ) {
        String username = principalUser.getUsername();
        ResponseDto<Void> response = userService.deleteUser(username, dto);
        HttpStatus status = response.isResult() ? HttpStatus.NO_CONTENT : HttpStatus.FORBIDDEN;
        return ResponseEntity.status(status).body(response);
    }
}