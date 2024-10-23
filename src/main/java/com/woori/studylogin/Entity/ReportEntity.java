package com.woori.studylogin.Entity;

import com.woori.studylogin.Constant.ReportCategoryType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name="report")
@Entity
public class ReportEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private ReportCategoryType category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_id")
    private BoardEntity board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private UserEntity user;
}
