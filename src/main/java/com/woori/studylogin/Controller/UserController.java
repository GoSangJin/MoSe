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
                               @PageableDefault(page = 0, size = 10) Pageable pageable,
                               Model model) {
        // 유저의 게시글 목록 가져오기
        Page<BoardDTO> userInfo = boardService.findByAuthor(username, pageable);

        Map<String, Integer> pageInfo = PaginationUtil.Pagination(userInfo);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("username", username);
        model.addAttribute("userInfo", userInfo);

        return "/user/other_user_page"; // 유저의 게시글 목록을 보여주는 템플릿 이름
    }
}
//Controller 작업 후 header.html에서 메뉴를 맵핑에 맞게 수정
//retrurn에 해당하는 html을 작성