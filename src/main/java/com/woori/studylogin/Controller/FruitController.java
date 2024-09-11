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
        FruitDTO fruitDTO = fruitService.read(id);
        model.addAttribute("fruitDTO", fruitDTO);
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);
        return "fruit/update";
    }

    @PostMapping("/fruit/update")
    public String updateFruit(@ModelAttribute FruitDTO fruitDTO,
                              @RequestParam("file") MultipartFile file,
                              RedirectAttributes redirectAttributes) throws IOException {

        fruitService.update(fruitDTO, file);

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
        FruitDTO fruitDTO = fruitService.read(id);
        model.addAttribute("fruitDTO", fruitDTO);
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);
        return "fruit/read";
    }

    // 전체조회(Get)
    @GetMapping("/fruit")
public String getAllFruits(@PageableDefault(page = 1) Pageable pageable,
                           @RequestParam(value = "searchType", defaultValue = "") String searchType,
                           @RequestParam(value = "search", defaultValue = "") String search, Model model) {

    Page<FruitDTO> fruitPage = fruitService.list(searchType, search, pageable);
    List<FruitDTO> fruitDTOList = fruitPage.getContent(); // 페이지에서 리스트만 추출
    model.addAttribute("fruitDTOList", fruitDTOList);
    model.addAttribute("bucket", bucket);
    model.addAttribute("region", region);
    model.addAttribute("folder", folder);

    for (FruitDTO fruit : fruitDTOList) {
        System.out.println(fruit.getFruitName() + ": " + fruit.getDiseaseDTOList());
    }


    Map<String, Integer> pageInfo = PaginationUtil.Pagination(fruitPage);
    model.addAllAttributes(pageInfo);

    model.addAttribute("searchType", searchType);
    model.addAttribute("search", search);

    return "fruit/list";
}

}
