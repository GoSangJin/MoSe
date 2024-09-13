package com.woori.studylogin.Controller;

import com.woori.studylogin.DTO.CommentDTO;
import com.woori.studylogin.Service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment/insert")
public String register(@RequestParam Integer no,
                       @ModelAttribute CommentDTO commentDTO, RedirectAttributes redirectAttributes) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    commentDTO.setNickName(username);

    commentService.register(no, commentDTO, username);
    return "redirect:/board/view?id=" + no;
}

    @GetMapping("/comment/remove")
public String remove(@RequestParam(name = "no", required = true) Integer no,
                     @RequestParam(name = "id", required = true) Integer id,
                     RedirectAttributes redirectAttributes) {
    commentService.remove(id);
    redirectAttributes.addFlashAttribute("message", "댓글이 성공적으로 삭제되었습니다.");
    return "redirect:/board/view?id=" + no;
}
}
