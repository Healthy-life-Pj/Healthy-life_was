package com.project.healthy_life_was.healthy_life.dto.physique.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class SetPhysiqueRequestDto {
    private Set<String> tagTypeNames;
}
