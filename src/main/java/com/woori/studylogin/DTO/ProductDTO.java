package com.woori.studylogin.DTO;

import com.woori.studylogin.Constant.RegionType;
import com.woori.studylogin.Constant.StatusType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductDTO {
    private Integer id; //체험정보 ID (Primary Key)
    private String local_name = "정보 없음"; // 기본값 설정 //지역 이름
    private String title; //마을 이름
    private String postcode; //우편번호
    private String address; //주소
    private String detailAddress;//상세주소
    private String extraAddress; //참고주소

    private LocalDate moveInDate; // 입주 가능일:

    // 신청 기간: 시작일과 종료일
    private LocalDate applicationStartDate;
    private LocalDate applicationEndDate;

    private Integer recruitmentCnt; //모집인원
    private String image_url; //마을이미지 URL
    @Enumerated(EnumType.STRING) //열거형 적용을 문자형으로
    private StatusType status; //진행상태 (모집중, 모집완료 등)
    private LocalDateTime modDate; //수정일자

    // 추가된 필드
    private Integer residents; // 주민 수
    private String representative; // 마을 대표자
    private String manager; // 마을 운영자
    private String supportCenter; // 시군 지원 센터

    private RegionType regionType;
}
