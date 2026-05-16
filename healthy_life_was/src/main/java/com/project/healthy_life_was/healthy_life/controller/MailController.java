package com.project.healthy_life_was.healthy_life.controller;

import com.project.healthy_life_was.healthy_life.common.constant.ApiMappingPattern;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.auth.request.FindIdRequestDto;
import com.project.healthy_life_was.healthy_life.dto.auth.request.FindInfoRequestDto;
import com.project.healthy_life_was.healthy_life.dto.auth.response.FindIdResponseDto;
import com.project.healthy_life_was.healthy_life.dto.user.request.PasswordUpdateRequestDto;
import com.project.healthy_life_was.healthy_life.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiMappingPattern.MAIL)
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    private final String FIND_ID_SEND_MAIL = "/find-id";
    private final String FIND_ID_BY_TOKEN = "/find-id/verify-find-username";
    private final String RECOVERY_PASSWORD_SEND_MAIL = "/recovery-email";
    private final String UPDATE_PASSWORD_BY_EMAIL = "/me/password/email";


    @PostMapping(FIND_ID_SEND_MAIL)
    public ResponseEntity<ResponseDto<String>> sendEmail(@RequestBody FindIdRequestDto dto) throws MessagingException {
        ResponseDto<String> response = mailService.sendMessageId(dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(FIND_ID_BY_TOKEN)
    public ResponseEntity<ResponseDto<FindIdResponseDto>> findLoginId(@RequestParam String token) {
        ResponseDto<FindIdResponseDto> response = mailService.verifyEmailId(token);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @PostMapping(RECOVERY_PASSWORD_SEND_MAIL)
    public ResponseEntity<ResponseDto<String>> sendPasswordEmail(@RequestBody FindInfoRequestDto dto) throws MessagingException {
        ResponseDto<String> response = mailService.sendMessagePw(dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @PutMapping(UPDATE_PASSWORD_BY_EMAIL)
    private ResponseEntity<ResponseDto<Void>> updatePwByEmailToken (
            @RequestBody PasswordUpdateRequestDto dto,
            @RequestParam @Valid String token
    ) {
        ResponseDto<Void> response = mailService.updatePwByEmailToken(token, dto);
        HttpStatus status = response.isResult() ? HttpStatus.NO_CONTENT : HttpStatus.FORBIDDEN;
        return ResponseEntity.status(status).body(response);
    }
}
