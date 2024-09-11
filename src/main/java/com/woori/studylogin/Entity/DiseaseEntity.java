package com.woori.studylogin.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "disease")

public class DiseaseEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;         // id
    private String description; // 설명
    @ElementCollection
//    @CollectionTable(name = "disease_images", joinColumns = @JoinColumn(name = "disease_id"))
    private List<String> diseaseImg; // 여러 개의 이미지 파일명을 저장하는 필드 추가  // 이미지
    private String diseaseName; // 병명
    private String cause;       // 원인
    private String prevention;  // 예방법

    @ManyToOne
    @JoinColumn(name = "fruit_id")
    private FruitEntity fruit; // 해당 과일
}
