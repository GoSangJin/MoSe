package com.woori.studylogin.Controller;

import com.woori.studylogin.DTO.BoardDTO;
import com.woori.studylogin.DTO.CommentDTO;
import com.woori.studylogin.DTO.UserDTO;
import com.woori.studylogin.Repository.CommentRepository;
import com.woori.studylogin.Service.BoardService;
import com.woori.studylogin.Service.CommentService;
import com.woori.studylogin.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MyPageController {
    private final CommentService commentService;
    private final UserService userService;
    private final BoardService boardService;
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;

    @GetMapping("/user/mypage")
    public String myPage(@RequestParam(value = "page", defaultValue = "0") int page,
                         @RequestParam(value = "commentPage", defaultValue = "0") int commPage,
                         @RequestParam(value = "size", defaultValue = "10") int size,
                         @RequestParam(value = "boardId", required = false) Integer boardId,
                         Model model) {

        // 현재 로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // 사용자 정보 가져오기
        UserDTO userDTO = userService.findByUsername(username);
        model.addAttribute("userDTO", userDTO);

        // 사용자가 작성한 게시글 목록 가져오기
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "regDate"));
        Page<BoardDTO> boardDTOS = boardService.myBoards(username, pageable);
        model.addAttribute("boardDTOS", boardDTOS);

        Pageable commPageable = PageRequest.of(commPage, size, Sort.by(Sort.Direction.DESC, "regDate"));
        Page<CommentDTO> commentPage = commentService.myComments(username, commPageable);
        model.addAttribute("commentsPage", commentPage);
        System.out.println(boardDTOS);
        System.out.println(commentPage);
        // 페이지와 사이즈 정보 추가
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);

        return "/user/mypage";
    }
}
