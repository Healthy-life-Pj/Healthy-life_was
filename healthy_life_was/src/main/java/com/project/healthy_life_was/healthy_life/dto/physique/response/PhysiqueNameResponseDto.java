package com.project.healthy_life_was.healthy_life.dto.physique.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class PhysiqueNameResponseDto {
    private Set<String> physiqueNames;
}
