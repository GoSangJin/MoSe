package com.woori.studylogin.Service;

import com.woori.studylogin.DTO.DocumentDTO;
import com.woori.studylogin.Entity.DocumentEntity;
import com.woori.studylogin.Repository.DocumentRepository;
import com.woori.studylogin.Util.DocumentUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentUpload documentUpload;

    // 공문 업로드
    public DocumentDTO uploadDocument(MultipartFile file, String title, String content) throws IOException {
        String filePath = documentUpload.upload(file, "documents"); // 전체 경로 반환

        DocumentEntity documentEntity = new DocumentEntity(title, content, file.getOriginalFilename(), filePath);
        documentRepository.save(documentEntity);

        String downloadUrl = documentUpload.generatePublicUrl(filePath);

        return new DocumentDTO(documentEntity.getId(), title, content, file.getOriginalFilename(), filePath, documentEntity.getUploadDate(), downloadUrl);
    }

    // 공문 목록 조회
    public Page<DocumentDTO> getDocuments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DocumentEntity> documentEntities = documentRepository.findAll(pageable);

        return documentEntities.map(entity -> {
            String downloadUrl = documentUpload.generatePublicUrl(entity.getFilePath());
            return new DocumentDTO(
                    entity.getId(),
                    entity.getTitle(),
                    entity.getContent(), // content 필드 추가
                    entity.getFileName(),
                    entity.getFilePath(),
                    entity.getUploadDate(),
                    downloadUrl // 추가된 필드
            );
        });
    }

    // 공문 상세 조회
    public DocumentDTO getDocumentById(Integer id) {
        Optional<DocumentEntity> documentEntity = documentRepository.findById(id);
        if (documentEntity.isPresent()) {
            DocumentEntity entity = documentEntity.get();
            String downloadUrl = documentUpload.generatePublicUrl(entity.getFilePath()); // 공개 URL 생성
            return new DocumentDTO(
                    entity.getId(),
                    entity.getTitle(),
                    entity.getContent(), // content 필드 추가
                    entity.getFileName(),
                    entity.getFilePath(),
                    entity.getUploadDate(),
                    downloadUrl // 추가된 필드
            );
        }
        return null;
    }

    // 공문 수정
    public DocumentDTO updateDocument(Integer id, MultipartFile file, String title, String content) throws IOException {
        Optional<DocumentEntity> optionalDocument = documentRepository.findById(id);
        if (optionalDocument.isPresent()) {
            DocumentEntity document = optionalDocument.get();
            if (file != null && !file.isEmpty()) {
                // 기존 파일 삭제
                try {
                    documentUpload.deleteFile(document.getFilePath(), "documents");
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the error appropriately
                }
                // 새 파일 업로드
                String filePath = documentUpload.upload(file, "documents");
                document.setFileName(file.getOriginalFilename());
                document.setFilePath(filePath);
            }
            document.setTitle(title);
            document.setContent(content); // content 필드 수정
            document.setUploadDate(LocalDateTime.now());

            documentRepository.save(document);
            String downloadUrl = documentUpload.generatePublicUrl(document.getFilePath());
            return new DocumentDTO(
                    document.getId(),
                    document.getTitle(),
                    document.getContent(), // content 필드 추가
                    document.getFileName(),
                    document.getFilePath(),
                    document.getUploadDate(),
                    downloadUrl // 추가된 필드
            );
        }
        return null;
    }

    // 공문 삭제
    public void deleteDocument(Integer id) {
        Optional<DocumentEntity> optionalDocument = documentRepository.findById(id);
        if (optionalDocument.isPresent()) {
            DocumentEntity document = optionalDocument.get();
            try {
                documentUpload.deleteFile(document.getFilePath(), "documents");
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the error appropriately
            }
            documentRepository.delete(document);
        }
    }

    // 파일 다운로드 URL 생성
    public String getFileDownloadUrl(Integer id) {
        Optional<DocumentEntity> documentEntity = documentRepository.findById(id);
        if (documentEntity.isPresent()) {
            DocumentEntity entity = documentEntity.get();
            return documentUpload.generatePublicUrl(entity.getFilePath()); // 공개 URL 생성
        }
        return null;
    }
}
