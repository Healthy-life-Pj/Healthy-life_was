package com.project.healthy_life_was.healthy_life.service.implement;

import com.project.healthy_life_was.healthy_life.common.constant.ResponseMessage;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.convertor.PhysiqueConvertor;
import com.project.healthy_life_was.healthy_life.dto.physique.request.SetPhysiqueRequestDto;
import com.project.healthy_life_was.healthy_life.dto.physique.response.PhysiqueNameResponseDto;
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

import java.util.*;

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
    public ResponseDto<PhysiqueNameResponseDto> getPhysiqueTag(String username) {
        PhysiqueNameResponseDto data = null;
        User user = findByUsername(username);
        data = physiqueConvertor.convertPhysiqueByUserId(user.getUserId());
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional
    public ResponseDto<PhysiqueTagResponseDto> setPhysiqueTag(
            String username,
            SetPhysiqueRequestDto dto
    ) {
        PhysiqueTagResponseDto data = null;
        User user = findByUsername(username);
        Set<String> tagNames = dto.getTagTypeNames();

        if (tagNames == null || tagNames.isEmpty()) {
            return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_DATA + "physiqueName");
        }

        if (tagNames.size() > 20) {
            return ResponseDto.setFailed(
                    ResponseMessage.VALIDATION_FAIL + "태그는 최대 20개까지만 선택 가능합니다"
            );
        }

        Set<Long> requestTagId = new HashSet<>();

        for (String tagName : tagNames) {
            if (tagName == null || tagName.trim().isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_DATA + "physiqueName");
            }

            Set<PhysiqueTag> tags =
                    physiqueTagRepository.findAllByPhysiqueName(tagName);

            if (tags.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_PHYSIQUE);
            }

            for (PhysiqueTag tag : tags) {
                requestTagId.add(tag.getPhysiqueTagId());
            }
        }

        Set<Long> currentTags =
                userPhysiqueTagRepository.findPhysiqueIdByUser_UserId(user.getUserId());

        if (currentTags == null) {
            currentTags = new HashSet<>();
        }

        Set<Long> tagsToAdd = new HashSet<>(requestTagId);
        tagsToAdd.removeAll(currentTags);

        Set<Long> tagsToRemove = new HashSet<>(currentTags);
        tagsToRemove.removeAll(requestTagId);

        for (Long tagId : tagsToAdd) {
            PhysiqueTag physiqueTag = physiqueTagRepository.findById(tagId)
                    .orElseThrow(() ->
                            new InternalException(ResponseMessage.NOT_EXIST_PHYSIQUE));

            UserPhysiqueTag newTag = UserPhysiqueTag.builder()
                    .userPhysiqueTagId(
                            new UserPhysiqueTag.UserPhysiqueTagId(
                                    user.getUserId(),
                                    physiqueTag.getPhysiqueTagId()
                            )
                    )
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
        data = new PhysiqueTagResponseDto(
                physiqueConvertor.convertToDtoByUserId(user.getUserId())
        );
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<PhysiqueNameResponseDto> getAllPhysiqueTag() {
        PhysiqueNameResponseDto data = null;

        data = new PhysiqueNameResponseDto(physiqueTagRepository.findAllTag());

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional
    public ResponseDto<Void> resetPhysiqueTag(String username) {
        User user = findByUsername(username);
        try {
            List<UserPhysiqueTag> userTag = userPhysiqueTagRepository.findAllByUser_UserId(user.getUserId());
            if(userTag == null || userTag.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_DATA + "userPhysiqueTag");
            }
            userPhysiqueTagRepository.deleteAll(userTag);
        } catch (DataAccessException e) {
            return ResponseDto.setFailed("Database error occurred");
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, null);
    }
}