package com.woori.studylogin.Config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
//메소드 : 프로그램을 수동으로 구동, 메소드를 실행
//이벤트(이벤트 핸들러) : 프로그램을 자동으로 구동
@Component
//사용자인증성공처리이벤트 인증성공이벤트 상속받아서 사용자 정의로 변경
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    //@Overrde 기존 메소드를 이름은 동일하게 사용하고, 내용은 변경
    //httpServlet(웹브라우저)Requset(요청), Response(응답)
    //try~catch나 throws(예외처리, catch)
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        //try { 외부장치, 외부연결처리-무한로딩, 무한대기, 프로그램이 강제종료
            HttpSession session = request.getSession(); //요청컴퓨터 정보받는다.
            //인증정보를 받는다.(아이디,비밀번호, 권한)
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            //setAttribute(변수에 값을 저장), 섹션(서버)에 변수(username)에 로그인한 아이디를 저장
            session.setAttribute("username", userDetails.getUsername());
            
            response.sendRedirect("/"); //브라우저에 /값으로 응답
        //} catch(IOException e) {
            
        //}
    }
}
