package com.project.healthy_life_was.healthy_life.service.implement;

import com.project.healthy_life_was.healthy_life.common.constant.ResponseMessage;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.qna.request.QnaRequestDto;
import com.project.healthy_life_was.healthy_life.dto.qna.response.QnaResponseDto;
import com.project.healthy_life_was.healthy_life.entity.product.Product;
import com.project.healthy_life_was.healthy_life.entity.qna.Qna;
import com.project.healthy_life_was.healthy_life.entity.user.User;
import com.project.healthy_life_was.healthy_life.repository.ProductRepository;
import com.project.healthy_life_was.healthy_life.repository.QnaRepository;
import com.project.healthy_life_was.healthy_life.repository.UserRepository;
import com.project.healthy_life_was.healthy_life.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QnaServiceImplement implements QnaService {

    private final QnaRepository qnaRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public ResponseDto<QnaResponseDto> createQna(String username, Long pId, QnaRequestDto dto) {
        QnaResponseDto data = null;

       String qnaTitle = dto.getQnaTitle();
       String qnaContent = dto.getQnaContent();

       try {
           User user = userRepository.findByUsername(username)
                   .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA + "user"));
           Product product = productRepository.findById(pId)
                   .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA + "product"));
           Qna qna = Qna.builder()
                   .product(product)
                   .user(user)
                   .qnaTitle(qnaTitle)
                   .qnaContent(qnaContent)
                   .build();
           qnaRepository.save(qna);

           data = new QnaResponseDto(qna);
       } catch (Exception e) {
           e.printStackTrace();
           return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
       }
       return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<List<QnaResponseDto>> getQnaAll() {
        List<QnaResponseDto> data = null;
        try{
            List<Qna> qna = qnaRepository.findAll();
            data = qna.stream()
                    .map(QnaResponseDto::new)
                    .toList();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<List<QnaResponseDto>> getQnaUser(String username) {
        List<QnaResponseDto> data = null;
        try{
            List<Qna> qna = qnaRepository.findByUser_Username(username);
            data = qna.stream()
                    .map(QnaResponseDto::new)
                    .toList();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<QnaResponseDto> updateQna(String username, Long qnaId, QnaRequestDto dto) {
        QnaResponseDto data = null;
        try{
            Optional<Qna> qnaOptional = qnaRepository.findById(qnaId);
            if(qnaOptional.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_DATA);
            }

            Qna qna = qnaOptional.get();

            if(!qna.getUser().getUsername().equals(username)){
                return ResponseDto.setFailed(ResponseMessage.NO_PERMISSION);
            }

            qna.setQnaTitle(dto.getQnaTitle());
            qna.setQnaContent(dto.getQnaContent());

            qnaRepository.save(qna);
            data = new QnaResponseDto(qna);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<Void> deleteQna(String username, Long qnaId) {
        try {
            Qna qna = qnaRepository.findByUser_UsernameAndQnaId(username, qnaId);
            if (qna == null) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_DATA);
            }
            qnaRepository.deleteById(qnaId);
        } catch (Exception e) {
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, null);
    }
}
