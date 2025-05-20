package com.project.healthy_life_was.healthy_life.controller;

import com.project.healthy_life_was.healthy_life.common.constant.ApiMappingPattern;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.qna.request.QnaRequestDto;
import com.project.healthy_life_was.healthy_life.dto.qna.response.QnaResponseDto;
import com.project.healthy_life_was.healthy_life.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.QNA)
@RequiredArgsConstructor
public class QnaController {

    private final QnaService qnaService;

    private final String QNA_POST = "/create/{pId}";
    private final String QNA_GET_USER = "/me";
    private final String QNA_PUT_USER = "/update/{qnaId}";
    private final String QNA_DELETE = "/{qnaId}";

    @PostMapping(QNA_POST)
    public ResponseEntity<ResponseDto<QnaResponseDto>> creatQna (
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long pId,
            @RequestBody QnaRequestDto dto
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<QnaResponseDto> response = qnaService.createQna(username, pId, dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<QnaResponseDto>>> getQnaAll () {
        ResponseDto<List<QnaResponseDto>> response = qnaService.getQnaAll();
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(QNA_GET_USER)
    public ResponseEntity<ResponseDto<List<QnaResponseDto>>> getQnaUser (
            @AuthenticationPrincipal UserDetails userDetails
            ) {
        if (userDetails == null) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<List<QnaResponseDto>> response = qnaService.getQnaUser(username);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @PutMapping(QNA_PUT_USER)
    public ResponseEntity<ResponseDto<QnaResponseDto>> updateQna (
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long qnaId,
            @RequestBody QnaRequestDto dto
    ) {
        if (userDetails == null) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<QnaResponseDto> response = qnaService.updateQna(username, qnaId, dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @DeleteMapping(QNA_DELETE)
    public ResponseEntity<ResponseDto<Void>> deleteQna (
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long qnaId
    ) {
        if (userDetails == null) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<Void> response = qnaService.deleteQna(username, qnaId);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }
}
