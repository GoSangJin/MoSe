package com.woori.studylogin.Constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlantType {
    FRUIT("과일"),  // 모집중
    VEGETABLE("채소"); // 모집완료

    private final String description;
}
