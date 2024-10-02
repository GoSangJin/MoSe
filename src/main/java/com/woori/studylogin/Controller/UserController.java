package com.woori.studylogin.Controller;

import com.woori.studylogin.DTO.BoardDTO;
import com.woori.studylogin.DTO.UserDTO;
import com.woori.studylogin.Service.BoardService;
import com.woori.studylogin.Service.UserService;
import com.woori.studylogin.Util.PaginationUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
public class UserController {
    private final BoardService boardService;
    private final UserService userService;

    //로그인
    @GetMapping("/login")
    public String login() {
        return "user/login"; //html이 있는 위치
    }

    //로그아웃(섹션에 값을 제거)
    //(http)-현재접속컴퓨터의 정보://localhost:8080/
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); //접속컴퓨터의 정보가 서버에 존재하면 정보를 제거
        return "redirect:/login";
    }
    //회원가입
    @GetMapping("/user/register") //가입폼으로 이동
    public String registerForm(Model model) {
        model.addAttribute("data", new UserDTO());

        return "user/register";
    }
    @PostMapping("/user/register")
    public String registerProc(UserDTO userDTO) {
        //받아온 값은 service에 처리
        userService.register(userDTO);

        return "redirect:/";
    }
    //회원수정
    @GetMapping("/user/update")
    //httpSession 섹션에 저장된 정보를 읽어온다.(로그인 정보)
    public String updateForm(HttpSession session, Model model) {
        //로그인 성공시 섹션에 사용자아이디가 저장
        //getAttribute(변수값 읽기)
        //session.setAttribute("username", ~); 핸들러
        String username = (String)session.getAttribute("username");
        log.info("{}현재 사용자", username);

        //auth.requestMatchers("/user/update").authenticated(); 대체가능
        if(username != null) { //로그인이 된 상태이면
            UserDTO userDTO = userService.detail(username); //회원정보를 읽어서
            model.addAttribute("data", userDTO); //수정페이지에 전달
        } else {
            return "redirect:/login";
        }

        return "/user/update";
    }
    @PostMapping("/user/update")
    public String updateProc(UserDTO userDTO, HttpSession session) { //화면에서 수정한 내용을 처리
        String username = (String)session.getAttribute("username");

        if(username != null) { //로그인 된 상태에서만 수정정보를 저장
            userService.update(userDTO);
        }
        return "redirect:/";
    }
    //다른 유저 게시글 목록 보기
    @GetMapping("/user/info")
    public String getUserPosts(@RequestParam String username,
                               @PageableDefault(page = 0, size = 15) Pageable pageable,
                               Model model) {
        // 유저의 게시글 목록 가져오기
        Page<BoardDTO> userInfo = boardService.findByAuthor(username, pageable);

        Map<String, Integer> pageInfo = PaginationUtil.Pagination(userInfo);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("username", username);
        model.addAttribute("userInfo", userInfo);

        return "/user/other_user_page"; // 유저의 게시글 목록을 보여주는 템플릿 이름
    }

    // 비밀번호 재입력 페이지 표시
    @GetMapping("/user/re_enter_credentials")
    public String showReEnterCredentialsPage(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login"; // 로그인 페이지로 리다이렉트
        }
        return "user/re_enter_credentials"; // 비밀번호 재입력 페이지 반환
    }

    // 비밀번호 재입력 처리
    @PostMapping("/user/re_enter_credentials")
    public String reEnterCredentials(UserDTO userDTO, HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        log.info("비밀번호 재입력을 시도하는 사용자: {}", username);

        // 로그인 상태 확인
        if (username == null) {
            model.addAttribute("error", "아이디가 일치하지 않습니다."); // 아이디가 없을 때 메시지
            return "user/re_enter_credentials"; // 재입력 페이지로 이동
        }

        // 입력된 아이디가 세션의 아이디와 일치하는지 확인
        if (!username.equals(userDTO.getUsername())) {
            model.addAttribute("error", "아이디가 일치하지 않습니다."); // 아이디가 존재하지 않을 때 메시지
            return "user/re_enter_credentials"; // 재입력 페이지로 이동
        }

        // 비밀번호 검증
        boolean isPasswordCorrect = userService.verifyPassword(username, userDTO.getPassword());

        if (!isPasswordCorrect) {
            model.addAttribute("error", "아이디와 비밀번호가 일치하지 않습니다."); // 비밀번호가 틀린 경우
            return "user/re_enter_credentials"; // 재입력 페이지로 이동
        }

        // 아이디와 비밀번호가 모두 일치하면 비밀번호 변경 페이지로 리다이렉트
        return "redirect:/user/change_password";  // 비밀번호 변경 페이지로 리다이렉트
    }

    // 비밀번호 변경 페이지
    @GetMapping("/user/change_password")
    public String showChangePasswordPage() {
        return "user/change_password"; // 비밀번호 변경 페이지 반환
    }

    // 비밀번호 변경 처리
    @PostMapping("/user/change_password")
    public String updatePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 HttpSession session,
                                 Model model) {
        String username = (String) session.getAttribute("username");

        if (username == null) {
            return "redirect:/login"; // 로그인 상태가 아닐 경우
        }

        // 현재 비밀번호 검증
        if (!userService.verifyPassword(username, currentPassword)) {
            model.addAttribute("errorMessage", "현재 비밀번호가 일치하지 않습니다.");
            return "user/change_password"; // 오류 메시지를 보여주기 위해 다시 비밀번호 변경 페이지로 이동
        }

        // 새 비밀번호와 비밀번호 확인이 일치하는지 확인
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return "user/change_password"; // 오류 메시지를 보여주기 위해 다시 비밀번호 변경 페이지로 이동
        }

        // 비밀번호 업데이트
        userService.updatePassword(username, newPassword);

        return "redirect:/"; // 비밀번호 변경 후 메인 페이지로 리다이렉트
    }
}
