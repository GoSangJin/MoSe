package com.woori.studylogin.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class QnaDTO {
    private Integer id;     // 일련번호
    private String title;   // 제목
    private String content; // 내용
}
