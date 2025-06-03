package com.project.healthy_life_was.healthy_life.controller;

import com.project.healthy_life_was.healthy_life.common.constant.ApiMappingPattern;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.auth.request.FindIdRequestDto;
import com.project.healthy_life_was.healthy_life.dto.auth.response.FindIdResponseDto;
import com.project.healthy_life_was.healthy_life.service.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiMappingPattern.MAIL)
@RequiredArgsConstructor
public class MailController {

    private final MailService MailService;

    private final String FIND_ID_SEND_MAIL = "/find-id";
    private final String FIND_ID_BY_TOKEN = "/find-id/verify-find-username";

    @PostMapping(FIND_ID_SEND_MAIL)
    public ResponseEntity<ResponseDto<String>> sendEmail(@RequestBody FindIdRequestDto dto) throws MessagingException {
        ResponseDto<String> response = MailService.sendMessageId(dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(FIND_ID_BY_TOKEN)
    public ResponseEntity<ResponseDto<FindIdResponseDto>> findLoginId(@RequestParam String token) {
        ResponseDto<FindIdResponseDto> response = MailService.verifyEmailId(token);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }
}
