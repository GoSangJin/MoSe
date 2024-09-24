package com.woori.studylogin.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class GuideDTO {
    private Integer id;
    private String title;
    private String description;
    private String processStep;
    private String additionalInfo;


    // Getters and Setters
}

