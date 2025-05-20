package com.project.healthy_life_was.healthy_life.service;

import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.physique.request.PhysiqueTagAddRequestDto;
import com.project.healthy_life_was.healthy_life.dto.physique.response.PhysiqueTagResponseDto;

public interface PhysiqueService {
    ResponseDto<PhysiqueTagResponseDto> getPhysiqueTag(String username);
    ResponseDto<PhysiqueTagResponseDto> setPhysiqueTag(String username, PhysiqueTagAddRequestDto dto);
    ResponseDto<PhysiqueTagResponseDto> getAllPhysiqueTag();
    ResponseDto<Void> resetPhysiqueTag(String username);
}