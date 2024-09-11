package com.woori.studylogin.DTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class FruitDTO {
    private Integer id;
    private String fruitName;                   // 작물이름
    private String fruitImg;                    // 이미지
    private List<DiseaseDTO> diseaseDTOList;    // 병해목록
}
