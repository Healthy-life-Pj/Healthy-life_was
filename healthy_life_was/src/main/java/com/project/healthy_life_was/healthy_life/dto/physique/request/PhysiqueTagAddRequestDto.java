package com.project.healthy_life_was.healthy_life.dto.physique.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class PhysiqueTagAddRequestDto {
    @Size(max = 20, message = "태그는 최대 20개까지만 선택 가능합니다")
    private Set<Long> physiqueTagIds = new HashSet<>();
}