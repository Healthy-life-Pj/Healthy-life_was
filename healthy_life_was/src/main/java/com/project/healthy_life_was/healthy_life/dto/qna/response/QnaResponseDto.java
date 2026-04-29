package com.project.healthy_life_was.healthy_life.dto.qna.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.healthy_life_was.healthy_life.entity.qna.Qna;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QnaResponseDto {
    private Long qnaId;
    @JsonProperty("pId")
    private Long pId;
    private String userNickName;
    private String qnaTitle;
    private String qnaContent;
    private String qnaAnswer;
    @JsonProperty("pName")
    private String pName;
    @JsonProperty("pImgUrl")
    private String pImgUrl;

    public QnaResponseDto(Qna qna) {
        this.qnaId = qna.getQnaId();
        this.pId = qna.getProduct().getPId();
        this.userNickName = qna.getUser().getUserNickName();
        this.qnaTitle = qna.getQnaTitle();
        this.qnaContent = qna.getQnaContent();
        this.qnaAnswer = qna.getQnaAnswer();
        this.pName = qna.getProduct().getPName();
        this.pImgUrl = qna.getProduct().getPImgUrl();
    }
}
