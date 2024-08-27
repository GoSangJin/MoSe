package com.woori.studylogin.DTO;

import com.woori.studylogin.Constant.BookType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class BookDTO {
    private Integer id; //일련번호
    private String title; //책제목
    private String author; //저자
    private String publisher; //브랜드
    private Integer price; //가격
    private BookType status; //상품 상태 (판매중, 품절 등)
    private String publishDate; //출판일
    private String idbn; //ISBN
    private String bookimg; //도서이미지
}