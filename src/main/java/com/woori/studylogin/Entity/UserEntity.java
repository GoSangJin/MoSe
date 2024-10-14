package com.woori.studylogin.Entity;

import com.woori.studylogin.Constant.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name="user")
@Entity
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  //일련번호
    private String username; //아이디
    private String password; //비밀번호
    private String email;
    private String birth;
    private String name; //사용자 이름
    private String postcode; //우편번호
    private String address; //주소
    private String detailAddress;//상세주소
    private String extraAddress; //참고주소
    @Enumerated(EnumType.STRING) //열거형 적용을 문자형으로
    private RoleType roleType;

    private boolean isSuspended;
    private LocalDate suspensionEndDate;

    // 유저가 삭제되면 유저의 게시글이 삭제
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardEntity> boards;

    // 유저가 삭제되면 유저의 댓글이 삭제
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeEntity> likes;
}