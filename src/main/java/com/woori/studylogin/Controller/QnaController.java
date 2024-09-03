package com.woori.studylogin.Controller;

import com.woori.studylogin.DTO.QnaDTO;
import com.woori.studylogin.Entity.QnaEntity;
import com.woori.studylogin.Repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;  // 여기에 Model 클래스를 임포트
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/qna")
public class QnaController {
    private final QnaRepository qnaRepository;

    //Q&A 등록페이지
    @GetMapping("/insert")
    public String insert() {
        return "qna/insert";
    }

    //Q&A 등록처리
    @PostMapping("/insert")
    public String insert(@ModelAttribute QnaDTO qnaDTO) {
        QnaEntity qnaEntity = QnaEntity.builder()
                .title(qnaDTO.getTitle())
                .content(qnaDTO.getContent())
                .build();
        qnaRepository.save(qnaEntity);
        return "redirect:/qna/list";

    }

    // QNA 리스트
    @GetMapping("/list")
    public String list(Model model) {
        List<QnaEntity> qnaList = qnaRepository.findAll();
        model.addAttribute("qnaList", qnaList);
        return "qna/list";
    }

    // QNA 수정 페이지
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable("id") Integer id, Model model) {
        QnaEntity qnaEntity = qnaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid QNA ID:" + id));
        QnaDTO qnaDTO = QnaDTO.builder()
                .id(qnaEntity.getId())
                .title(qnaEntity.getTitle())
                .content(qnaEntity.getContent())
                .build();
        model.addAttribute("qnaDTO", qnaDTO);
        return "qna/update";
    }

    // QNA 수정 처리
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id, @ModelAttribute QnaDTO qnaDTO) {
        QnaEntity qnaEntity = qnaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid QNA ID:" + id));
        qnaEntity.setTitle(qnaDTO.getTitle());
        qnaEntity.setContent(qnaDTO.getContent());
        qnaRepository.save(qnaEntity);
        return "redirect:/qna/list";
    }

    // QNA 삭제
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        qnaRepository.deleteById(id);
        return "redirect:/qna/list";
    }
}
