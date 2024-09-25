package com.woori.studylogin.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder

@Entity
@Table(name="comment")
@SequenceGenerator(
        name="comment_seq", sequenceName="comment_seq",
        initialValue = 1, allocationSize = 1
)
public class CommentEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commnent_seq")
    private Integer id;

    @Column(name="body", length=100)
    private String body;

    @Column(name="nickName",length=20)
    private String nickName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private BoardEntity board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private UserEntity user;
}