package com.woori.studylogin.Controller;

import com.woori.studylogin.DTO.QnaDTO;
import com.woori.studylogin.Service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class QnaController {
    private final QnaService qnaService;

    // Q&A 목록 페이지
    @GetMapping("/qna/list")
    public String list(Model model) {
        List<QnaDTO> qnaDTOList = qnaService.list();
        model.addAttribute("list", qnaDTOList);
        return "qna/list"; // qna/list.html 반환
    }

    // Q&A 삽입 페이지 (GET)
    @GetMapping("/qna/insert")
    public String insertView(Model model) {
        model.addAttribute("qna", new QnaDTO()); // 빈 QnaDTO를 모델에 추가하여 폼에서 사용할 수 있도록 함
        return "qna/insert"; // qna/insert.html 반환
    }

    // Q&A 삽입 처리 (POST)
    @PostMapping("/qna/insert")
    public String insertPost(@ModelAttribute QnaDTO qnaDTO) {
        qnaService.insert(qnaDTO);
        return "redirect:/qna/list"; // 삽입 후 목록 페이지로 리다이렉트
    }

    // Q&A 수정 페이지 (GET)
    @GetMapping("/qna/update/{id}")
    public String updateView(@PathVariable Integer id, Model model) {
        QnaDTO qnaDTO = qnaService.detail(id);
        model.addAttribute("qna", qnaDTO); // 수정할 QnaDTO를 모델에 추가
        return "qna/update"; // qna/update.html 반환
    }

    // Q&A 수정 처리 (POST)
    @PostMapping("/qna/update/{id}")
    public String updateQna(@PathVariable("id") Integer id, @ModelAttribute QnaDTO qnaDTO) {
        qnaDTO.setId(id); // 수정할 QnaDTO의 ID 설정
        qnaService.update(qnaDTO); // 업데이트 처리
        return "redirect:/qna/list"; // 수정 후 목록 페이지로 리다이렉트
    }

    // Q&A 상세보기 페이지
    @GetMapping("/qna/detail/{id}")
    public String detailView(@PathVariable Integer id, Model model) {
        QnaDTO qnaDTO = qnaService.detail(id);
        model.addAttribute("list", qnaDTO); // 상세 내용을 모델에 추가
        return "qna/detail"; // qna/detail.html 반환
    }

    // Q&A 삭제 처리
    @GetMapping("/qna/delete/{id}")
    public String deleteProc(@PathVariable Integer id) {
        qnaService.delete(id); // 삭제 처리
        return "redirect:/qna/list"; // 삭제 후 목록 페이지로 리다이렉트
    }
}
