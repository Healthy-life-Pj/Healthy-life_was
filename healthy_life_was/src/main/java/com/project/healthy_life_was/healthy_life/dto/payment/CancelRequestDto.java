package com.project.healthy_life_was.healthy_life.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelRequestDto {
    private String impUid;
    private String merchantUid;
    private Long amount;
    private String reason;
}
