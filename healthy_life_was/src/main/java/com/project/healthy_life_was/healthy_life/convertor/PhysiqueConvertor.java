package com.project.healthy_life_was.healthy_life.convertor;

import com.project.healthy_life_was.healthy_life.common.constant.ResponseMessage;
import com.project.healthy_life_was.healthy_life.dto.physique.response.PhysiqueResponseDto;
import com.project.healthy_life_was.healthy_life.entity.physique.PhysiqueTag;
import com.project.healthy_life_was.healthy_life.repository.PhysiqueTagRepository;
import com.project.healthy_life_was.healthy_life.repository.UserPhysiqueTagRepository;
import com.sun.jdi.InternalException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Data
public class PhysiqueConvertor {
    private final UserPhysiqueTagRepository userPhysiqueTagRepository;
    private final PhysiqueTagRepository physiqueTagRepository;

    // userId로 Dto로 변환
    public List<PhysiqueResponseDto> convertToDtoByUserId (Long userId) {
        List<PhysiqueResponseDto> result = null;
        Set<Long> userPhysiqueIds = userPhysiqueTagRepository.findByUserId(userId);
        if(userPhysiqueIds == null){
            userPhysiqueIds = new HashSet<>();
        }
        result = userPhysiqueIds
                .stream()
                .map((id) -> {
                    return new PhysiqueResponseDto(id, physiqueTagRepository
                            .findById(id)
                            .orElseThrow(() -> new InternalException(ResponseMessage.NOT_EXIST_PHYSIQUE))
                            .getPhysiqueName());
                })
                .collect(Collectors.toList());
        return result;
    }

    // 전체 조회를 Dto로 변환
    public List<PhysiqueResponseDto> convertAllToDto() {
        List<PhysiqueResponseDto> result = null;

        try {
            List<PhysiqueTag> tags = physiqueTagRepository.findAll();
            result = tags.stream()
                    .map((tag) -> new PhysiqueResponseDto(tag.getPhysiqueTagId(), tag.getPhysiqueName()))
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    // physiqueId로 Dto로 변환(단건)
    public PhysiqueResponseDto convertToDtoByPhysiqueId (Long physiqueId) {
        PhysiqueResponseDto result = null;
        PhysiqueTag physiqueTag = physiqueTagRepository.findById(physiqueId)
                .orElseThrow(() -> new InternalException(ResponseMessage.FAIL_TO_CONVERT));

        result = new PhysiqueResponseDto(physiqueTag.getPhysiqueTagId(),physiqueTag.getPhysiqueName());
        return result;
    }
}