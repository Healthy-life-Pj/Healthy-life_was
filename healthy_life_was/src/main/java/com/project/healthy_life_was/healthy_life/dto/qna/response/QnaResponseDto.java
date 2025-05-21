package com.project.healthy_life_was.healthy_life.dto.qna.response;

import com.project.healthy_life_was.healthy_life.entity.qna.Qna;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QnaResponseDto {
    private Long qnaId;
    private Long pId;
    private String username;
    private String qnaTitle;
    private String qnaContent;
    private String qnaAnswer;

    public QnaResponseDto(Qna qna) {
        this.qnaId = qna.getQnaId();
        this.pId = qna.getProduct().getPId();
        this.username = qna.getUser().getUsername();
        this.qnaTitle = qna.getQnaTitle();
        this.qnaContent = qna.getQnaContent();
        this.qnaAnswer = qna.getQnaAnswer();
    }
}
