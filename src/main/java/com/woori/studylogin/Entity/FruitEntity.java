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
@Table(name = "fruit")

public class FruitEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FruitEntity_SEQ")
    private Integer Id;
    private String fruitName;        // 과일이름
    private String fruitImg;    // 과일 이미지 경로

    @OneToMany(mappedBy = "fruit", cascade = CascadeType.ALL)
    private List<DiseaseEntity> diseases; // 병해 목록
}
