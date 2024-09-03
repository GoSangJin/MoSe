package com.woori.studylogin.Controller;

import com.woori.studylogin.DTO.CommentDTO;
import com.woori.studylogin.Service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    @PostMapping("/comment/insert")
    public String register(Integer no, CommentDTO commentDTO) {
        commentService.register(no, commentDTO);
        return "redirect:/board/view?id="+no;
    }

    @GetMapping("/comment/remove")
    public String remove(Integer no, Integer id) {
        commentService.remove(id);
        return "redirect:/board/view?id="+no;
    }
}
