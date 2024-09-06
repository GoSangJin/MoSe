package com.woori.studylogin.Constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusType {
    OPEN("모집중"),  // 모집중
    CLOSED("모집완료"); // 모집완료

    private final String description;
}
//열거형 : 숫자를 단어로 대신 정해서 이용
//단어선정이 중요
