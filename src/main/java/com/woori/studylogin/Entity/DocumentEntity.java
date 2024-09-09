package com.woori.studylogin.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name="document")
@Entity
public class DocumentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private LocalDateTime uploadDate;

    public DocumentEntity(String title,String content ,String fileName, String filePath) {
        this.title = title;
        this.content = content;
        this.fileName = fileName;
        this.filePath = filePath;
        this.uploadDate = LocalDateTime.now();
    }
}
