package com.project.healthy_life_was.healthy_life.dto.physique.response;

import lombok.Data;

import java.util.List;

@Data
public class PhysiqueTagResponseDto {
    private List<PhysiqueResponseDto> physiqueTagList;

    public PhysiqueTagResponseDto (List<PhysiqueResponseDto> dto) {
        this.physiqueTagList = dto;
    }
}