package com.woori.studylogin.Entity;

import com.woori.studylogin.Constant.CategoryType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name="board")
@Entity
@SequenceGenerator(
        name="article_seq", sequenceName="article_seq",
        initialValue = 1, allocationSize = 1
)
public class BoardEntity extends BaseEntity {
    //기본 최대 크기는 255byte
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "article_seq")
    private Integer id; //일련번호
    @Column(name="title", length = 100)
    private String title; // 글 제목
    private String author; //작성자
    @Lob //65535byte(Large Object)
    @Column(name="content" ,columnDefinition = "TEXT")
    private String content; // 글 내용
    @Column(name="viewCount")
    private Integer viewCount=0;
//    @Column(name="category")
//    @NotNull
//    private String category;
    @Column(name="likeCount")
    private Integer likeCount=0;
    private String boardImg = null;

    @Enumerated(EnumType.STRING)
    private CategoryType category;
    @Column(name="commentCount")
    private Integer commentCount = 0;

    @OneToMany(mappedBy = "board",cascade = CascadeType.ALL)
    private List<CommentEntity> comments;

    @OneToMany(mappedBy = "board",cascade = CascadeType.ALL)
    private List<LikeEntity> likes;

    @OneToMany(mappedBy = "board",cascade = CascadeType.ALL)
    private List<ReportEntity> reports;
}
