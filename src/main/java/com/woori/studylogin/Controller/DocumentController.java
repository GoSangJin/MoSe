package com.woori.studylogin.Controller;

import com.woori.studylogin.DTO.DocumentDTO;
import com.woori.studylogin.Service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    // 공문 목록 페이지
    @GetMapping("/document/list")
    public String listDocuments(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, Model model) {
        Page<DocumentDTO> documentPage = documentService.getDocuments(page, size);
        model.addAttribute("documents", documentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", documentPage.getTotalPages());
        return "document/list"; // list.html
    }

    // 공문 업로드 페이지
    @GetMapping("/document/insert")
    public String showUploadForm() {
        return "document/insert"; // insert.html
    }

    // 공문 업로드 처리
    @PostMapping("/document/insert")
    public String uploadDocument(@RequestParam("file") MultipartFile file, @RequestParam("title") String title, @RequestParam("content") String content) {
        try {
            documentService.uploadDocument(file, title, content);
        } catch (IOException e) {
            e.printStackTrace();
            // 에러 처리 로직 추가 필요
        }
        return "redirect:/document/list";
    }

    // 공문 상세 페이지
    @GetMapping("/document/detail/{id}")
    public String getDocumentDetail(@PathVariable Integer id, Model model) {
        DocumentDTO document = documentService.getDocumentById(id);
        if (document != null) {
            String downloadUrl = documentService.getFileDownloadUrl(id); // 다운로드 URL 가져오기
            model.addAttribute("document", document);
            model.addAttribute("downloadUrl", downloadUrl); // URL을 모델에 추가
            return "document/detail"; // detail.html
        } else {
            // 에러 페이지 없이 처리
            return "redirect:/document/list";
        }
    }

    // 공문 수정 페이지
    @GetMapping("/document/update/{id}")
    public String showUpdateForm(@PathVariable Integer id, Model model) {
        DocumentDTO document = documentService.getDocumentById(id);
        if (document != null) {
            model.addAttribute("document", document);
            return "document/update"; // update.html
        } else {
            // 에러 페이지 없이 처리
            return "redirect:/document/list";
        }
    }

    // 공문 수정 처리
    @PostMapping("/document/update/{id}")
    public String updateDocument(@PathVariable Integer id, @RequestParam("file") MultipartFile file, @RequestParam("title") String title, @RequestParam("content") String content) {
        try {
            documentService.updateDocument(id, file, title, content);
        } catch (IOException e) {
            e.printStackTrace();
            // 에러 처리 로직 추가 필요
        }
        return "redirect:/document/list";
    }

    // 공문 삭제 처리
    @PostMapping("/document/delete/{id}")
    public String deleteDocument(@PathVariable Integer id) {
        documentService.deleteDocument(id);
        return "redirect:/document/list"; // 삭제 후 목록 페이지로 리다이렉트
    }
}
