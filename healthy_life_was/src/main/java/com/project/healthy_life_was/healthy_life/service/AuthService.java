package com.project.healthy_life_was.healthy_life.service;

import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.auth.request.FindInfoRequestDto;
import com.project.healthy_life_was.healthy_life.dto.auth.request.LoginRequestDto;
import com.project.healthy_life_was.healthy_life.dto.auth.request.SignUpRequestDto;
import com.project.healthy_life_was.healthy_life.dto.auth.response.FindInfoResponseDto;
import com.project.healthy_life_was.healthy_life.dto.auth.response.LoginResponseDto;
import com.project.healthy_life_was.healthy_life.dto.auth.response.SignUpResponseDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;

public interface AuthService {
    ResponseDto<SignUpResponseDto> signUp(@Valid SignUpRequestDto dto);

    ResponseDto<LoginResponseDto> login(@Valid LoginRequestDto dto);

    ResponseDto<Boolean> duplicateUserName(@Valid String username);

    ResponseDto<Boolean> duplicateUserNickName(@Valid String userNickName);

    ResponseDto<FindInfoResponseDto> recoveryEmail(FindInfoRequestDto dto) throws MessagingException;
}