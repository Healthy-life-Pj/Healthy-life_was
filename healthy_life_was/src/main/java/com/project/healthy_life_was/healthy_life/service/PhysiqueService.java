package com.project.healthy_life_was.healthy_life.service;

import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.physique.request.SetPhysiqueRequestDto;
import com.project.healthy_life_was.healthy_life.dto.physique.response.PhysiqueNameResponseDto;
import com.project.healthy_life_was.healthy_life.dto.physique.response.PhysiqueTagResponseDto;

public interface PhysiqueService {
    ResponseDto<PhysiqueNameResponseDto> getPhysiqueTag(String username);
    ResponseDto<PhysiqueTagResponseDto> setPhysiqueTag(String username, SetPhysiqueRequestDto dto);
    ResponseDto<PhysiqueNameResponseDto> getAllPhysiqueTag();
    ResponseDto<Void> resetPhysiqueTag(String username);
}