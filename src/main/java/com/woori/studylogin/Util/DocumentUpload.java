package com.woori.studylogin.Util;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DocumentUpload {

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 파일 업로드
    private String upload(File uploadFile, String filePath) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, filePath, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return filePath;
    }


    // 파일 삭제
    public void deleteFile(String deleteFile, String dirName) throws IOException {
        String filePath = dirName + "/" + deleteFile;
        try {
            amazonS3Client.deleteObject(bucket, filePath);
        } catch (SdkClientException e) {
            System.out.println("오류발생: " + e.getMessage());
        }
    }

    // MultipartFile을 S3에 업로드
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new RuntimeException("파일 변환 실패: " + multipartFile.getOriginalFilename()));
        String fileName = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();
        String filePath = dirName + "/" + fileName; // 전체 경로 설정
        upload(uploadFile, filePath); // 전체 경로 사용
        removeNewFile(uploadFile); // 업로드 후 파일 삭제
        return filePath; // 경로 반환
    }

    // 파일을 S3에 업로드 (기존 메서드 삭제됨)
    private void removeNewFile(File targetFile) {
        targetFile.delete();
    }

    // MultipartFile을 File로 변환
    private Optional<File> convert(MultipartFile multipartFile) {
        try {
            File convertFile = new File(System.getProperty("user.dir") + "/" + multipartFile.getOriginalFilename());
            if (convertFile.createNewFile()) {
                try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                    fos.write(multipartFile.getBytes());
                }
                return Optional.of(convertFile);
            }
        } catch (IOException e) {
            System.out.println("파일 변환 오류: " + e.getMessage());
        }
        return Optional.empty();
    }

    // Presigned URL 생성
    public String generatePublicUrl(String filePath) {
        return "https://kosangjin.s3.ap-northeast-2.amazonaws.com/" + filePath;
    }
}
