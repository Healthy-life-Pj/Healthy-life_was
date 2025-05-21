package com.project.healthy_life_was.healthy_life.controller;

import com.project.healthy_life_was.healthy_life.common.constant.ApiMappingPattern;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.qna.response.QnaResponseDto;
import com.project.healthy_life_was.healthy_life.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.AUTH)
@RequiredArgsConstructor
public class QnaAuthController {

    private final QnaService qnaService;

    private final String QNA_GET = "/qnas/{pId}";

    @GetMapping(QNA_GET)
    public ResponseEntity<ResponseDto<List<QnaResponseDto>>> getQnaPid (@PathVariable Long pId) {
        ResponseDto<List<QnaResponseDto>> response = qnaService.getQnaPid(pId);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }
}
