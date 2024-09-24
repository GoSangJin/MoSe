package com.woori.studylogin.DTO;

import lombok.Data;

@Data
public class ReportDTO {
    private Integer id;
    private String title;
    private String description;
    private Integer boardId; // 추가된 필드
    private String boardTitle;
    private String boardAuthor;
    private String reporterUsername;
}
