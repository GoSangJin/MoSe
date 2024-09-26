package com.woori.studylogin.Controller;


import com.woori.studylogin.DTO.BoardDTO;
import com.woori.studylogin.DTO.CommentDTO;
import com.woori.studylogin.DTO.UserDTO;
import com.woori.studylogin.Entity.UserEntity;
import com.woori.studylogin.Repository.UserRepository;
import com.woori.studylogin.Service.BoardService;
import com.woori.studylogin.Service.CommentService;
import com.woori.studylogin.Service.UserService;
import com.woori.studylogin.Util.PaginationUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//DTO와 Service를 이용해서 생성(맵핑명을 알려주면)
//삽입은 register, 수정은 update, 삭제는 remove, 개별조회는 detail, 전체조회는 list로 맵핑되게 작성해줘
@Log4j2
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final UserRepository userRepository;
    @Value("${cloud.aws.s3.bucket}")
    public String bucket; //버킷명
    @Value("${cloud.aws.region.static}")
    public String region; //저장지역
    @Value("${imgUploadLocation}")
    public String folder; //버킷에 이용할 폴더명

    private final UserService userService;
    private final CommentService commentService;
    private final BoardService boardService;

    //삽입(Get, Post)
    @GetMapping("/board/new")
    public String showCreateForm(Model model) {
        model.addAttribute("boardDTO", new BoardDTO());
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);
        return "board/insert";
    }

    // 게시글 생성 처리
    @PostMapping("/board/new")
    //BoardDTO-입력폼 받은값, HTTPSession-섹션정보, RedirectAttributes-다른맵핑에 값전달
    public String createBoard(@ModelAttribute BoardDTO boardDTO, MultipartFile file , HttpSession session,
                              RedirectAttributes redirectAttributes)throws IOException {
        //사용자 아이디를 읽어서
        //
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String)session.getAttribute("username");
        
        // 신고 추가된 구문
        UserEntity user = userRepository.findByUsername(username)
                                        .orElseThrow(() -> new RuntimeException("User not found"));

        // 사용자 정지 상태 확인
        if (user.isSuspended()) {
        String remainingPeriod = userService.getRemainingSuspensionPeriod(user);
        String errorMessage = "작성이 제한된 계정입니다.<br>" +
                " 자세한 사항은 관리자에게 문의하십시오.<br>"  +
                remainingPeriod;
        redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        return "redirect:/board/error";
        }
        //여기까지 신고추가 
        
        boardDTO.setAuthor(username); //아이디를 작성자에 저장해서 서비스로 전달
        boardService.save(boardDTO,file);

        redirectAttributes.addFlashAttribute("message", "게시글이 성공적으로 생성되었습니다.");
        return "redirect:/board/list";
    }
    //수정(Get, Post)
    // 게시글 수정 폼
    @GetMapping("/board/edit")
    public String showUpdateForm(@RequestParam Integer id, Model model, RedirectAttributes redirectAttributes) {

        //권한부여를 위한 코드
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean isAdminOrMaster = authentication.getAuthorities().stream().anyMatch(auth ->
        auth.getAuthority().equals("ROLE_admin") ||
        auth.getAuthority().equals("ROLE_master"));


        BoardDTO boardDTO = boardService.findById(id,"U");
        UserDTO userDTO = userService.detail(username);

        if (!username.equals(boardDTO.getAuthor()) && !isAdminOrMaster) {
        redirectAttributes.addFlashAttribute("error", "권한이 없습니다.");
        return "redirect:/board/list";
    }
        model.addAttribute("userDTO", userDTO);
        model.addAttribute("boardDTO", boardDTO);
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);
        return "board/update";
    }

    // 게시글 수정 처리
    @PostMapping("/board/edit")
    public String updateBoard(@ModelAttribute BoardDTO boardDTO,
                              @RequestParam(value = "file", required = false) MultipartFile file,
                              @RequestParam(value = "removeImage", required = false) String removeImage,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) throws IOException {
        //사용자 아이디를 읽어서
        //
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean isAdminOrMaster = authentication.getAuthorities().stream().anyMatch(auth ->
        auth.getAuthority().equals("ROLE_admin") ||
        auth.getAuthority().equals("ROLE_master"));
        // 작성자 본인이거나 관리자만 수정 가능
        if (!username.equals(boardDTO.getAuthor()) && !isAdminOrMaster) {
            redirectAttributes.addFlashAttribute("error", "권한이 없습니다.");
            return "redirect:/board/list";
        }


        boardDTO.setAuthor(username); // 아이디를 작성자에 저장해서 서비스로 전달
        boolean removeImgFlag = "true".equals(removeImage);  // 이미지 삭제 여부 확인

        boardService.update(boardDTO, removeImgFlag, file);


        redirectAttributes.addFlashAttribute("message", "게시글이 성공적으로 수정되었습니다.");
        return "redirect:/board/list";

    }
    //삭제(Get)
    @GetMapping("/board/delete")
    public String deleteBoard(@RequestParam Integer id, RedirectAttributes redirectAttributes) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean isAdminOrMaster = authentication.getAuthorities().stream().anyMatch(auth ->
        auth.getAuthority().equals("ROLE_admin") ||
        auth.getAuthority().equals("ROLE_master"));


        BoardDTO boardDTO = boardService.findById(id, "U");
        if (!username.equals(boardDTO.getAuthor()) && !isAdminOrMaster) {
            redirectAttributes.addFlashAttribute("error", "권한이 없습니다.");
            return "redirect:/board/list";
        }


        boardService.delete(id);
        redirectAttributes.addFlashAttribute("message", "게시글이 성공적으로 삭제되었습니다.");
        return "redirect:/board/list";
    }
    //개별조회(Get)
    // 게시글 개별 조회
    @GetMapping("/board/view")
    public String getBoardById(@RequestParam Integer id,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "10") int size,
                               Model model, HttpSession session) {
        BoardDTO boardDTO = boardService.findById(id,"R");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean isAdminOrMaster = authentication.getAuthorities().stream().anyMatch(auth ->
        auth.getAuthority().equals("ROLE_admin") ||
        auth.getAuthority().equals("ROLE_master"));

        UserDTO userDTO = userService.detail(username);

        Pageable pageable = PageRequest.of(page, size);
        Page<CommentDTO> commentDTOS = commentService.getCommentsByPage(id, pageable);

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);
        model.addAttribute("boardDTO", boardDTO);
        model.addAttribute("commentDTOS", commentDTOS);
        model.addAttribute("username", username);
        System.out.println("UserDTO RoleType: " + userDTO.getRoleType()); // RoleType 값 확인

        // 사용자가 이미 추천했는지 여부 확인
        boolean hasLiked = false; // 기본값 false
        if (username != null) {
            hasLiked = boardService.hasLiked(id, username); // 데이터베이스에서 좋아요 여부 확인
        }
        model.addAttribute("hasLiked", hasLiked);

        // Pagination info
        Map<String, Integer> pageInfo = PaginationUtil.Pagination(commentDTOS);
        model.addAttribute("pageInfo", pageInfo);

        return "board/view";
    }
    //전체조회(Get)x
    // 게시글 전체 조회 (페이지 처리)
//    @GetMapping("/board/list")
//    public String getAllBoards(@PageableDefault(page=1) Pageable pageable,
//                               Model model) {
//        Page<BoardDTO> boardPage = boardService.findAll(pageable);
//        model.addAttribute("list", boardPage);
//        Map<String, Integer> pageInfo = PaginationUtil.Pagination(boardPage);
//        model.addAllAttributes(pageInfo);
//
//        return "board/list";
//    }

    // 검색기능 구현
    @GetMapping("/board/list")
    public String list(@RequestParam(value = "searchType", defaultValue = "") String searchType,
                       @RequestParam(value = "searchKeyword", defaultValue = "") String searchKeyword,
                       @RequestParam(value = "category", defaultValue = "") String category,
                       @PageableDefault(page = 0) Pageable pageable, Model model) {
        log.info("검색구분과 검색어를 받아서 자료를 찾아 전달");
        log.info("페이지 번호를 받아서 전달");

        int currentPage = pageable.getPageNumber();
        List<BoardDTO> noticeBoards = currentPage == 0 ? boardService.getNoticeBoards("공지사항") : new ArrayList<>();

        if (currentPage == 0) {
        model.addAttribute("noticeBoards", noticeBoards);
    }

        Page<BoardDTO> boardDTOS = boardService.list(searchType, searchKeyword, category, pageable);
        model.addAttribute("boardDTOS", boardDTOS);

        // Add additional attributes as needed
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("searchType", searchType);
        model.addAttribute("category", category);

        // Pagination info
        Map<String, Integer> pageInfo = PaginationUtil.Pagination(boardDTOS);
        model.addAttribute("pageInfo", pageInfo);

        return "board/list";
    }
    //좋아요 구문
    @GetMapping("/board/like")
    public String recommend(@RequestParam Integer id, HttpSession session, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            if (boardService.hasLiked(id, username)) {
                boardService.decreaseLikeCount(id, username);
                session.setAttribute("hasLiked_" + id, false);
                redirectAttributes.addFlashAttribute("message", "추천이 취소되었습니다.");
            } else {
                boardService.increaseLikeCount(id, username);
                session.setAttribute("hasLiked_" + id, true);
                redirectAttributes.addFlashAttribute("message", "추천이 반영되었습니다.");
            }
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/board/view?id=" + id;
    }
    // 에러페이지 통합
    @GetMapping("board/error")
    public String showErrorPage(Model model) {
    return "/board/error"; // 'error.html'을 반환하여 에러 페이지를 표시합니다.
}
}
// 게시글등록/수정/삭제-로그인시에만 노출
//