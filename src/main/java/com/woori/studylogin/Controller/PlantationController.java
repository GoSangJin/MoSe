package com.woori.studylogin.Controller;

import com.woori.studylogin.Constant.PlantType;
import com.woori.studylogin.DTO.PlantationDTO;
import com.woori.studylogin.Service.PlantationService;
import com.woori.studylogin.Util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Map;

//DTO와 Service를 이용해서 생성(맵핑명을 알려주면)
//삽입은 register, 수정은 update, 삭제는 remove, 개별조회는 detail, 전체조회는 list로 맵핑되게 작성해줘

@Controller
@RequiredArgsConstructor
public class PlantationController {
    @Value("kosangjin")
    public String bucket; //버킷명
    @Value("ap-northeast-2")
    public String region; //저장지역
    @Value("${imgUploadLocation}")
    public String folder; //버킷에 이용할 폴더명

    private final PlantationService plantationService;

    //삽입(Get, Post)
    @GetMapping("/plantation/insert")
    public String showCreateForm(Model model) {
        model.addAttribute("plantationDTO", new PlantationDTO());
        model.addAttribute("plantTypes", PlantType.values()); // 모든 PlantTypes을 모델에 추가
        model.addAttribute("selectedStatus", PlantType.FRUIT); // 현재 상품의 상태
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);
        return "plantation/insert";
    }

    // 상품 생성 처리
    @PostMapping("/plantation/insert")
    public String createPlantation(@ModelAttribute PlantationDTO plantationDTO,
                                MultipartFile file,
                                RedirectAttributes redirectAttributes) throws IOException {

        plantationService.save(plantationDTO, file);

        redirectAttributes.addFlashAttribute("message", "상품이 성공적으로 생성되었습니다.");
        return "redirect:/plantation/list";
    }
    //수정(Get, Post)
    // 상품 수정 폼
    @GetMapping("/plantation/update")
    public String showUpdateForm(@RequestParam Integer id, Model model) {
        PlantationDTO plantationDTO = plantationService.findById(id);

        // 모델에 상태와 관련된 정보를 추가
        model.addAttribute("plantTypes", PlantType.values()); // 모든 PlantTypes을 모델에 추가
        model.addAttribute("selectedStatus", plantationDTO.getStatus()); // 현재 상품의 상태
        model.addAttribute("plantationDTO", plantationDTO);
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);

        return "plantation/update";
    }

    // 상품 수정 처리
    @PostMapping("/plantation/update")
    public String updatePlantation(@ModelAttribute PlantationDTO plantationDTO,
                                MultipartFile file,
                                RedirectAttributes redirectAttributes) throws IOException {

        plantationService.update(plantationDTO, file);

        redirectAttributes.addFlashAttribute("message", "상품이 성공적으로 수정되었습니다.");
        return "redirect:/plantation/list";
    }
    //삭제(Get)
    @GetMapping("/plantation/delete")
    public String deletePlantation(@RequestParam Integer id, RedirectAttributes redirectAttributes) throws Exception {
        plantationService.delete(id);
        redirectAttributes.addFlashAttribute("message", "상품이 성공적으로 삭제되었습니다.");
        return "redirect:/plantation/list";
    }
    //개별조회(Get)
    // 상품 개별 조회
    @GetMapping("/plantation/detail")
    public String getPlantationById(@RequestParam Integer id, Model model) {
        PlantationDTO plantationDTO = plantationService.findById(id);
        model.addAttribute("plantationDTO", plantationDTO);
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);
        return "plantation/detail";
    }
    // 상품 전체 조회 (페이지 처리)
    @GetMapping("/plantation/list")
    public String getAllPlantations(
            @PageableDefault(page = 0, size = 12) Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) PlantType status, // 분류 추가
            Model model) {

        Page<PlantationDTO> plantationPage;

        if (status != null) {
            plantationPage = plantationService.findByStatus(status, pageable);
        } else {
            plantationPage = plantationService.findAll(pageable, search);
        }

        model.addAttribute("list", plantationPage);
        model.addAttribute("plantTypes", PlantType.values());
        model.addAttribute("selectedStatus", status); // 현재 선택된 분류 추가
        model.addAttribute("search", search); // 검색어 추가
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);

        Map<String, Integer> pageInfo = PaginationUtil.Pagination(plantationPage);
        model.addAllAttributes(pageInfo);

        return "plantation/list";
    }
}
