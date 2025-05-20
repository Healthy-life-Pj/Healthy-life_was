package com.project.healthy_life_was.healthy_life.dto.qna.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QnaRequestDto {

    private String qnaTitle;
    private String qnaContent;

}
