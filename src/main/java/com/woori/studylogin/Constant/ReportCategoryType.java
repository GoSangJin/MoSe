package com.woori.studylogin.Constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportCategoryType {

    POLITICS("정치적 발언"),
    SEXUAL("음란물 및 성적 발언"),
    ABUSE("욕설 및 폭언"),
    SPAM("도배글");

    private final String description;
}
