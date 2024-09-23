package com.woori.studylogin.Entity;

import com.woori.studylogin.Constant.RegionType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "event")

public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private String eventName;
    private String description;
    private String eventImg;
    private String eventUrl;
    @Enumerated(EnumType.STRING) // 열거형을 문자형으로 저장
    @Column(nullable = false) // 필수
    private RegionType regionType;
}
