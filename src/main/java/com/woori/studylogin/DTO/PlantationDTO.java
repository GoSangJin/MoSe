package com.woori.studylogin.DTO;

import com.woori.studylogin.Constant.PlantType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PlantationDTO {
    private Integer id; // 일련번호
    private String title; // 과일채소/이름
    private String efficacy; // 효능
    private String region; // 적응지역
    private String harvest; // 수확시기
    private String cultivation; // 재배방법
    private String characteristics; // 특징
    private String precautions; // 유의사항
    private PlantType status; // 식물 분류
    private String thumbnail_img; // 썸네일 이미지


}
