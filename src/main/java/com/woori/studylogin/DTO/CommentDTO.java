package com.woori.studylogin.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CommentDTO {
    private Integer id;
    private String body;
    private String nickName;
    private LocalDateTime modDate;
}
