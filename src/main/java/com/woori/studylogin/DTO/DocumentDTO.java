package com.woori.studylogin.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Builder
public class DocumentDTO {
    private Integer id;
    private String title;
    private String content;
    private String fileName;
    private String filePath;
    private LocalDateTime uploadDate;
    private String downloadUrl; // 추가된 필드


    // 추가된 생성자
    public DocumentDTO(Integer id, String title,String content ,String fileName, String filePath, LocalDateTime uploadDate, String downloadUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.fileName = fileName;
        this.filePath = filePath;
        this.uploadDate = uploadDate;
        this.downloadUrl = downloadUrl;
    }

    // Getter and Setter for all fields
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public LocalDateTime getUploadDate() { return uploadDate; }
    public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }

    public String getDownloadUrl() { return downloadUrl; }
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
}
