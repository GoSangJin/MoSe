package com.woori.studylogin.Controller;

import com.woori.studylogin.DTO.FruitDTO;
import com.woori.studylogin.Service.FruitService;
import com.woori.studylogin.Util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class FruitController {

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    @Value("${cloud.aws.region.static}")
    public String region;

    @Value("${imgUploadLocation}")
    public String folder;

    private final FruitService fruitService;

    // 삽입(Get, Post)
    @GetMapping("/fruit/create")
    public String showCreateForm(Model model) {
        model.addAttribute("fruitDTO", new FruitDTO());
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);
        return "fruit/create";
    }

    @PostMapping("/fruit/create")
    public String createFruit(@ModelAttribute FruitDTO fruitDTO,
                              @RequestParam("file") MultipartFile file,
                              RedirectAttributes redirectAttributes) throws IOException {

        fruitService.create(fruitDTO, file);

        redirectAttributes.addFlashAttribute("message", "과일이 성공적으로 생성되었습니다.");
        return "redirect:/fruit";
    }

    // 수정(Get, Post)
    @GetMapping("/fruit/update")
    public String showUpdateForm(@RequestParam Integer id, Model model) {
        FruitDTO fruitDTO = fruitService.findById(id);
        model.addAttribute("fruitDTO", fruitDTO);
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);
        return "fruit/update";
    }

    @PostMapping("/fruit/update")
    public String updateFruit(@ModelAttribute FruitDTO fruitDTO,
                              @RequestParam(value = "file", required = false) MultipartFile file,
                              @RequestParam(value = "removeImage", required = false) String removeImage,
                              RedirectAttributes redirectAttributes) throws IOException {

        fruitService.update(fruitDTO, file);
        
        if ("true".equals(removeImage)) {
                fruitDTO.setFruitImg(null); // 이미지 필드를 null로 설정하여 이미지 삭제
            }
        // 파일이 null이 아니고 비어있지 않은 경우에만 파일 처리
            if (file != null && !file.isEmpty()) {
                fruitService.update(fruitDTO, file);
            } else {
                fruitService.update(fruitDTO, null); // 파일이 없을 경우 파일 파라미터를 null로 전달
            }
        redirectAttributes.addFlashAttribute("message", "과일이 성공적으로 수정되었습니다.");
        return "redirect:/fruit";
    }

    // 삭제(Get)
    @GetMapping("/fruit/delete")
    public String deleteFruit(@RequestParam Integer id) throws Exception {
        fruitService.delete(id);
        return "redirect:/fruit";
    }

    // 개별조회(Get)
    @GetMapping("/fruit/read")
    public String getFruitById(@RequestParam Integer id, Model model) {
        FruitDTO fruitDTO = fruitService.findById(id);
        model.addAttribute("fruitDTO", fruitDTO);
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);
        return "fruit/read";
    }

    // 전체조회(Get)
@GetMapping("/fruit")
public String getAllFruits(@PageableDefault(page = 1) Pageable pageable,
                           @RequestParam(value = "searchType", defaultValue = "name") String searchType, // 검색 유형 기본값을 'name'으로 설정
                           @RequestParam(value = "search", defaultValue = "") String search, Model model) {

    // 검색 조건에 맞는 과일 목록을 가져옴
    Page<FruitDTO> fruitPage = fruitService.list(searchType, search, pageable);
    List<FruitDTO> fruitDTOList = fruitPage.getContent(); // 페이지에서 리스트만 추출
    model.addAttribute("fruitDTOList", fruitDTOList);
    model.addAttribute("bucket", bucket);
    model.addAttribute("region", region);
    model.addAttribute("folder", folder);

    Map<String, Integer> pageInfo = PaginationUtil.Pagination(fruitPage);
    model.addAllAttributes(pageInfo);

    model.addAttribute("searchType", searchType);
    model.addAttribute("search", search);

    return "fruit/list";
}

}
