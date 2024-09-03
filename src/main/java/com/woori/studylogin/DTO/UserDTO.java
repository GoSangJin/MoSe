package com.woori.studylogin.DTO;

import com.woori.studylogin.Constant.RoleType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserDTO {
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
    private RoleType roleType;
    private LocalDateTime modDate;
}
