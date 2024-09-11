package com.woori.studylogin.DTO;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class DiseaseDTO {
    private Integer id;
    private String description; // 설명
    private List<String> diseaseImg;  // 이미지
    private String diseaseName; // 병명
    private String cause;       // 원인
    private String prevention;  // 예방법
    private LocalDateTime modDate;
    private Integer fruitId;

}
