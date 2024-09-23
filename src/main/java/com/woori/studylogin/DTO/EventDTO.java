package com.woori.studylogin.DTO;

import com.woori.studylogin.Constant.RegionType;
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
    private RegionType regionType;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
