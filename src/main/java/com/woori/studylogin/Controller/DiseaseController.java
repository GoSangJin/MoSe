package com.woori.studylogin.Controller;

import com.woori.studylogin.DTO.DiseaseDTO;
import com.woori.studylogin.Service.DiseaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class DiseaseController {

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    @Value("${cloud.aws.region.static}")
    public String region;

    @Value("${imgUploadLocation}")
    public String folder;

    private final DiseaseService diseaseService;

    // 삽입(Get, Post)
    @GetMapping("/disease/create")
    public String showCreateForm(Model model,@RequestParam("fruitId") Integer fruitId) {
        // DiseaseDTO 객체를 생성하고 fruitId를 설정
        DiseaseDTO diseaseDTO = new DiseaseDTO();
        diseaseDTO.setFruitId(fruitId);

        model.addAttribute("diseaseDTO",diseaseDTO);
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);
        return "disease/create";
    }

    @PostMapping("/disease/create")
    public String createDisease(@RequestParam("fruitId") Integer fruitId,
                                @ModelAttribute DiseaseDTO diseaseDTO,
                                @RequestParam("files") MultipartFile[] files,
                                RedirectAttributes redirectAttributes) throws IOException {
        // fruitId가 null인지 확인
        if (fruitId == null) {
            // 예외 처리 또는 기본값 설정
            throw new IllegalArgumentException("Fruit ID must not be null");
        }

        // diseaseDTO에 fruitId 설정
        diseaseDTO.setFruitId(fruitId);

        diseaseService.create(diseaseDTO, files);

        redirectAttributes.addFlashAttribute("message", "병해가 성공적으로 생성되었습니다.");
        return "redirect:/fruit";
    }

    // 수정(Get, Post)
    @GetMapping("/disease/update")
    public String showUpdateForm(@RequestParam Integer id, Model model) {
        DiseaseDTO diseaseDTO = diseaseService.read(id);
        model.addAttribute("diseaseDTO", diseaseDTO);
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);
        return "disease/update";
    }

    @PostMapping("/disease/update")
    public String updateDisease(@ModelAttribute DiseaseDTO diseaseDTO,
                                @RequestParam("files") MultipartFile[] files,
                                RedirectAttributes redirectAttributes) throws IOException {

        diseaseService.update(diseaseDTO, files);

        redirectAttributes.addFlashAttribute("message", "병해가 성공적으로 수정되었습니다.");
        return "redirect:/fruit";
    }

    // 삭제(Get)
    @GetMapping("/disease/delete")
    public String deleteDisease(@RequestParam Integer id) throws Exception {
        diseaseService.delete(id);
        return "redirect:/fruit";
    }

    // 개별조회(Get)
    @GetMapping("/disease/read")
    public String getDiseaseById(@RequestParam Integer id, Model model) {
        DiseaseDTO diseaseDTO = diseaseService.read(id);
        model.addAttribute("diseaseDTO", diseaseDTO);
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);
        return "disease/read";
    }

    // 전체조회(Get)
//    @GetMapping("/disease")
//    public String getAllDiseases(@PageableDefault(page=1) Pageable pageable,
//                                 @RequestParam(value="searchType", defaultValue="") String searchType,
//                                 @RequestParam(value="search", defaultValue="") String search, Model model) {
//
//        Page<DiseaseDTO> diseaseDTOList = diseaseService.list(searchType, search, pageable);
//        model.addAttribute("diseaseDTOList", diseaseDTOList);
//        model.addAttribute("bucket", bucket);
//        model.addAttribute("region", region);
//        model.addAttribute("folder", folder);
//
//        Map<String, Integer> pageInfo = PaginationUtil.Pagination(diseaseDTOList);
//        model.addAllAttributes(pageInfo);
//
//        model.addAttribute("searchType", searchType);
//        model.addAttribute("search", search);
//
//        return "disease/list";
//    }
}
