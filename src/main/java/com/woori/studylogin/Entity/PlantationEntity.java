package com.woori.studylogin.Entity;

import com.woori.studylogin.Constant.PlantType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name="plantation")
@Entity
public class PlantationEntity extends BaseEntity {
    //기본 최대 크기는 255byte
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //일련번호
    private String title; //과일채소/이름
    private String efficacy;
    private String region;
    private String harvest;
    @Column(columnDefinition = "TEXT")
    private String cultivation; // 재배방법
    @Column(columnDefinition = "TEXT")
    private String characteristics; // 특징
    @Column(columnDefinition = "TEXT")
    private String precautions; // 유의사항
    @Enumerated(EnumType.STRING) // 열거형 적용을 문자형으로
    private PlantType status; // 식물 분류
    private String thumbnail_img; // 썸네일 이미지
}
