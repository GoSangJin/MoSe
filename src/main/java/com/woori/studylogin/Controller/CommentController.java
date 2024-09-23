package com.woori.studylogin.Controller;

import com.woori.studylogin.DTO.CommentDTO;
import com.woori.studylogin.Entity.UserEntity;
import com.woori.studylogin.Repository.UserRepository;
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

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;

    @PostMapping("/comment/insert")
public String register(@RequestParam Integer no,
                       @ModelAttribute CommentDTO commentDTO, RedirectAttributes redirectAttributes) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    commentDTO.setNickName(username);

    // 신고 추가된 구문
        UserEntity user = userRepository.findByUsername(username)
                                        .orElseThrow(() -> new RuntimeException("User not found"));

        // 사용자 정지 상태 확인
        if (user.isSuspended() && (user.getSuspensionEndDate() == null || LocalDate.now().isBefore(user.getSuspensionEndDate()))) {
            redirectAttributes.addFlashAttribute("errorMessage", "게시글 작성이 금지된 계정입니다. 자세한 사항은 관리자에게 문의하십시오.");
            return "redirect:/board/error";
        }
        //여기까지 신고추가

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
