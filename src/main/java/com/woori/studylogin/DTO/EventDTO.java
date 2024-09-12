package com.woori.studylogin.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class EventDTO {

    private Integer id;
    private String eventName;
    private String description;
    private String eventImg;
    private String eventUrl;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
