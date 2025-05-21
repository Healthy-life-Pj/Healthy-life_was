package com.project.healthy_life_was.healthy_life.service;

import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.qna.request.QnaRequestDto;
import com.project.healthy_life_was.healthy_life.dto.qna.response.QnaResponseDto;

import java.util.List;

public interface QnaService {

    ResponseDto<QnaResponseDto> createQna(String username, Long pId, QnaRequestDto dto);

    ResponseDto<List<QnaResponseDto>> getQnaPid(Long pId);

    ResponseDto<List<QnaResponseDto>> getQnaUser(String username);

    ResponseDto<QnaResponseDto> updateQna(String username, Long qnaId, QnaRequestDto dto);

    ResponseDto<Void> deleteQna(String username, Long qnaId);
}
