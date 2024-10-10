package com.woori.studylogin.Controller;

import com.woori.studylogin.DTO.UserDTO;
import com.woori.studylogin.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@Controller
@RequiredArgsConstructor
public class PasswordResetController {
    private final UserService userService;
    private final JavaMailSender mailSender;
    private String tempEmail;
    private String tempCode;

    @GetMapping("/user/find-password")
    public String findPasswordForm() {
        return "user/find-password"; // 비밀번호 찾기 페이지
    }

    @PostMapping("/user/find-password")
    public String findPassword(@RequestParam String username, @RequestParam String name, @RequestParam String birth,
                               Model model) {
        // 아이디, 이름, 생년월일 확인
        UserDTO userDTO = userService.findByUsername(username);
        if (userDTO != null && userDTO.getName().equals(name) && userDTO.getBirth().equals(birth)) {
            tempEmail = userDTO.getEmail();
            tempCode = generateRandomCode();
            sendVerificationEmail(tempEmail, tempCode);
            model.addAttribute("username", username);
            return "user/verify-code"; // 인증번호 입력 페이지
        }
        model.addAttribute("error", "정보가 일치하지 않습니다.");
        return "user/find-password"; // 다시 비밀번호 찾기 페이지
    }

    @PostMapping("/user/verify-code")
    public String verifyCode(@RequestParam String username, @RequestParam String inputCode, Model model) {
        if (inputCode.equals(tempCode)) {
            model.addAttribute("username", username);
            return "user/new-password"; // 비밀번호 변경 페이지
        }
        model.addAttribute("error", "인증번호가 일치하지 않습니다.");
        return "user/verify-code"; // 다시 인증번호 입력 페이지
    }

    @PostMapping("/user/new-password")
    public String resetPassword(@RequestParam String username, @RequestParam String newPassword) {
        userService.updatePassword(username, newPassword);
        return "redirect:/login"; // 로그인 페이지로 리다이렉트
    }

    private String generateRandomCode() {
        Random random = new Random();
        return String.valueOf(random.nextInt(999999)); // 6자리 랜덤 코드 생성
    }

    private void sendVerificationEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("비밀번호 찾기 인증번호");
        message.setText("인증번호: " + code);
        mailSender.send(message);
    }
}
