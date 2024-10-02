package com.woori.studylogin.Controller;


import com.woori.studylogin.Util.Flask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class YoloController {
    private final Flask flask;

    @GetMapping("/diagnose")
    public String start(Model model) throws Exception {
        return "/diagnose/insert";
    }

    @PostMapping("/diagnose/result")
    public String result(@RequestParam("file") MultipartFile file, Model model) throws Exception {
        // 플라스크 서버에 분석할 이미지를 전달하여 처리하고 결과를 반환받음
        Map<String, Object> result = flask.requestToFlask(file);

        // 모델에 데이터 추가
        model.addAttribute("result", result);
        model.addAttribute("labels", result.get("labels"));
        model.addAttribute("crops", result.get("crops"));


        // 결과 페이지 반환
        return "/diagnose/result"; // Thymeleaf 템플릿 이름
    }
}