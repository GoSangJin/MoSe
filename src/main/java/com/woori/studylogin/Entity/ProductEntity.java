package com.woori.studylogin.Entity;

import com.woori.studylogin.Constant.StatusType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name="product")
@Entity
public class ProductEntity extends BaseEntity {
    //기본 최대 크기는 255byte
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // 체험정보 ID (Primary Key)

    @Column(nullable = false, length = 255) // 필수, 최대 255자
    private String title; // 마을 이름

    @Column(nullable = false, length = 255) // 필수, 최대 255자
    private String local_name; // 지역 이름

    @Column(length = 20) // 최대 20자
    private String postcode; // 우편번호

    @Column(nullable = false, length = 255) // 필수, 최대 255자
    private String address; // 주소

    @Column(length = 255) // 최대 255자
    private String detailAddress; // 상세주소
    @Column(length = 255) // 최대 255자
    private String extraAddress; // 참고주소

    private LocalDate moveInDate; // 입주 가능일

    // 신청 기간: 시작일과 종료일
    private LocalDate applicationStartDate;
    private LocalDate applicationEndDate;

    private Integer recruitmentCnt; // 모집인원
    @Column(length = 255) // 최대 255자
    private String image_url; // 마을 이미지 URL

    @Enumerated(EnumType.STRING) // 열거형을 문자형으로 저장
    @Column(nullable = false) // 필수
    private StatusType status; // 진행상태 (모집중, 모집완료 등)

    // 추가된 필드
    private Integer residents; // 주민 수
    @Column(length = 255) // 최대 255자
    private String representative; // 마을 대표자
    @Column(length = 255) // 최대 255자
    private String manager; // 마을 운영자
    @Column(length = 255) // 최대 255자
    private String supportCenter; // 시군 지원 센터
}
//변수선언 부분, 변수명과 데이터형을 정확하게 작업
