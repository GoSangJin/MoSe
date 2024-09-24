package com.woori.studylogin.Controller;

import com.woori.studylogin.DTO.GuideDTO;
import com.woori.studylogin.Service.GuideService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/guide")
public class GuideController {
    private final GuideService guideService;

    public GuideController(GuideService guideService) {
        this.guideService = guideService;
    }

    @GetMapping
    public ResponseEntity<List<GuideDTO>> getAllGuides() {
        List<GuideDTO> guides = guideService.getAllGuides();
        return ResponseEntity.ok(guides);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuideDTO> getGuideById(@PathVariable Integer id) {
        GuideDTO guideDTO = guideService.getGuideById(id);
        return ResponseEntity.ok(guideDTO);
    }

    @PostMapping
    public ResponseEntity<GuideDTO> createGuide(@RequestBody GuideDTO guideDTO) {
        GuideDTO createdGuide = guideService.createGuide(guideDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGuide);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuide(@PathVariable Integer id) {
        guideService.deleteGuide(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @GetMapping("/info")
    public String getGuideInfo(Model model) {
        List<GuideDTO> guides = guideService.getAllGuides(); // 가이드 리스트 가져오기
        model.addAttribute("guideList", guides); // 모델에 가이드 리스트 추가
        return "guide/info"; // 슬래시 없이
    }

}
