package com.project.healthy_life_was.healthy_life.service.implement;

import com.project.healthy_life_was.healthy_life.common.constant.ResponseMessage;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.convertor.PhysiqueConvertor;
import com.project.healthy_life_was.healthy_life.dto.physique.request.PhysiqueTagAddRequestDto;
import com.project.healthy_life_was.healthy_life.dto.physique.response.PhysiqueTagResponseDto;
import com.project.healthy_life_was.healthy_life.entity.physique.PhysiqueTag;
import com.project.healthy_life_was.healthy_life.entity.physique.UserPhysiqueTag;
import com.project.healthy_life_was.healthy_life.entity.user.User;
import com.project.healthy_life_was.healthy_life.repository.PhysiqueTagRepository;
import com.project.healthy_life_was.healthy_life.repository.UserPhysiqueTagRepository;
import com.project.healthy_life_was.healthy_life.repository.UserRepository;
import com.project.healthy_life_was.healthy_life.service.PhysiqueService;
import com.sun.jdi.InternalException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PhysiqueServiceImplement implements PhysiqueService {
    private final UserRepository userRepository;
    private final UserPhysiqueTagRepository userPhysiqueTagRepository;
    private final PhysiqueTagRepository physiqueTagRepository;
    private final PhysiqueConvertor physiqueConvertor;

    private User findByUsername (String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InternalException(ResponseMessage.NOT_EXIST_USER));
        return user;
    }

    @Override
    public ResponseDto<PhysiqueTagResponseDto> getPhysiqueTag(String username) {
        PhysiqueTagResponseDto data = null;

        User user = findByUsername(username);

        data = new PhysiqueTagResponseDto(physiqueConvertor.convertToDtoByUserId(user.getUserId()));

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional
    public ResponseDto<PhysiqueTagResponseDto> setPhysiqueTag(String username, PhysiqueTagAddRequestDto dto) {
        PhysiqueTagResponseDto data = null;
        User user = findByUsername(username);
        Set<Long> tags = dto.getPhysiqueTagIds();

        for (Long tagId : tags) {
            if (tagId == null || tagId <= 0) {
                return ResponseDto.setFailed(ResponseMessage.VALIDATION_FAIL + "tagId");
            }
            if (!physiqueTagRepository.existsById(tagId)) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_PHYSIQUE);
            }
        }

        if (tags.size() > 20) {
            return ResponseDto.setFailed(ResponseMessage.VALIDATION_FAIL + "태그는 최대 20개까지만 선택 가능합니다");
        }

        Set<Long> currentTags = userPhysiqueTagRepository.findByUserId(user.getUserId());
        if (currentTags == null) {
            currentTags = new HashSet<>();
        }

        for(Long currentTagId : currentTags){
            physiqueTagRepository.findById(currentTagId)
                    .orElseThrow(() -> new InternalException(ResponseMessage.NOT_EXIST_PHYSIQUE));
        }

        if(!tags.isEmpty()) {
            Set<Long> tagsToAdd = new HashSet<>(tags);
            tagsToAdd.removeAll(currentTags);

            Set<Long> tagsToRemove = new HashSet<>(currentTags);
            tagsToRemove.removeAll(tags);

            for (Long tagId : tagsToAdd) {
                PhysiqueTag physiqueTag = physiqueTagRepository.findById(tagId)
                        .orElseThrow(() -> new InternalException(ResponseMessage.NOT_EXIST_PHYSIQUE));

                UserPhysiqueTag newTag = UserPhysiqueTag.builder()
                        .userPhysiqueTagId(new UserPhysiqueTag.UserPhysiqueTagId(user.getUserId(), tagId))
                        .user(user)
                        .physiqueTag(physiqueTag)
                        .build();

                userPhysiqueTagRepository.save(newTag);
            }

            for (Long tagId : tagsToRemove) {
                userPhysiqueTagRepository.deleteByUserPhysiqueTagId(
                        new UserPhysiqueTag.UserPhysiqueTagId(user.getUserId(), tagId)
                );
            }

            data = new PhysiqueTagResponseDto(physiqueConvertor.convertToDtoByUserId(user.getUserId()));
        } else {
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, new PhysiqueTagResponseDto(new ArrayList<>()));
        }

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<PhysiqueTagResponseDto> getAllPhysiqueTag() {
        PhysiqueTagResponseDto data = null;

        data = new PhysiqueTagResponseDto(physiqueConvertor.convertAllToDto());

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional
    public ResponseDto<Void> resetPhysiqueTag(String username) {
        User user = findByUsername(username);
        try {
            userPhysiqueTagRepository.deleteAllByUserId(user.getUserId());
        } catch (DataAccessException e) {
            return ResponseDto.setFailed("Database error occurred");
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, null);
    }

}
