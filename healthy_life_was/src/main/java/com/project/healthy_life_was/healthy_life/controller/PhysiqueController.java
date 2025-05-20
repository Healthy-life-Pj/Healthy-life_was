package com.project.healthy_life_was.healthy_life.controller;

import com.project.healthy_life_was.healthy_life.common.constant.ApiMappingPattern;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.physique.request.PhysiqueTagAddRequestDto;
import com.project.healthy_life_was.healthy_life.dto.physique.response.PhysiqueTagResponseDto;
import com.project.healthy_life_was.healthy_life.security.PrincipalUser;
import com.project.healthy_life_was.healthy_life.service.PhysiqueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiMappingPattern.USER)
@RequiredArgsConstructor
public class PhysiqueController {
    private final String USER_CREATE_PHYSIQUE = "/me/physiques";
    private final String USER_PHYSIQUE = "/me/physiques";
    private final String ALL_PHYSIQUE = "/physiques";

    private final PhysiqueService physiqueService;

    @PutMapping(USER_CREATE_PHYSIQUE)
    public ResponseEntity<ResponseDto<PhysiqueTagResponseDto>> setPhysiqueTag (
            @AuthenticationPrincipal PrincipalUser principalUser,
            @RequestBody @Valid PhysiqueTagAddRequestDto dto
    ) {
        String username = principalUser.getUsername();
        ResponseDto<PhysiqueTagResponseDto> response = physiqueService.setPhysiqueTag(username, dto);
        HttpStatus status = response.isResult()? HttpStatus.OK: HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(USER_PHYSIQUE)
    public ResponseEntity<ResponseDto<PhysiqueTagResponseDto>> getPhysiqueTag (
            @AuthenticationPrincipal PrincipalUser principalUser
            ) {
        String username = principalUser.getUsername();
        ResponseDto<PhysiqueTagResponseDto> response = physiqueService.getPhysiqueTag(username);
        HttpStatus status = response.isResult()? HttpStatus.OK: HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(ALL_PHYSIQUE)
    public ResponseEntity<ResponseDto<PhysiqueTagResponseDto>> getAllPhysiqueTag (
            @AuthenticationPrincipal PrincipalUser principalUser
    ) {
        ResponseDto<PhysiqueTagResponseDto> response = physiqueService.getAllPhysiqueTag();
        HttpStatus status = response.isResult()? HttpStatus.OK: HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @DeleteMapping(USER_PHYSIQUE)
    public ResponseEntity<ResponseDto<Void>> resetPhysiqueTag (
            @AuthenticationPrincipal PrincipalUser principalUser
    ) {
        String username = principalUser.getUsername();
        ResponseDto<Void> response = physiqueService.resetPhysiqueTag(username);
        HttpStatus status = response.isResult() ? HttpStatus.NO_CONTENT : HttpStatus.FORBIDDEN;
        return ResponseEntity.status(status).body(response);
    }
}