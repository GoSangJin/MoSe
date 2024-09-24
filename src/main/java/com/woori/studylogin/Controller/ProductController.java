package com.woori.studylogin.Controller;

import com.woori.studylogin.Constant.StatusType;
import com.woori.studylogin.DTO.ProductDTO;
import com.woori.studylogin.Service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

//DTO와 Service를 이용해서 생성(맵핑명을 알려주면)
//삽입은 register, 수정은 update, 삭제는 remove, 개별조회는 detail, 전체조회는 list로 맵핑되게 작성해줘

@Controller
@RequiredArgsConstructor
@Log4j2
public class ProductController {
    @Value("kosangjin")
    public String bucket; //버킷명
    @Value("ap-northeast-2")
    public String region; //저장지역
    @Value("${imgUploadLocation}")
    public String folder; //버킷에 이용할 폴더명

    private final ProductService productService;

    //삽입(Get, Post)
    @GetMapping("/product/new")
    public String showCreateForm(Model model) {
        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("statusTypes", StatusType.values());
        model.addAttribute("selectedStatus", StatusType.OPEN);
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);
        return "/product/insert"; // 수정: 경로 변경
    }

    // 상품 생성 처리

    // 상품 생성 처리
    @PostMapping("/product/new")
    public String createProduct(@ModelAttribute ProductDTO productDTO,
                                @RequestParam MultipartFile file, // 수정: @RequestParam 추가
                                RedirectAttributes redirectAttributes) throws IOException {
        productService.save(productDTO, file);
        redirectAttributes.addFlashAttribute("message", "상품이 성공적으로 생성되었습니다.");
        return "redirect:/product/list";
    }
    //수정(Get, Post)
    // 상품 수정 폼
    @GetMapping("/product/edit/{id}") // 수정: URL 패턴 변경
    public String showUpdateForm(@PathVariable Integer id, Model model) { // 수정: @PathVariable 변경
        ProductDTO productDTO = productService.findById(id);
        model.addAttribute("statusTypes", StatusType.values());
        model.addAttribute("selectedStatus", productDTO.getStatus());
        model.addAttribute("productDTO", productDTO);
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);
        return "product/update";
    }

    // 상품 수정 처리
    @PostMapping("/product/edit")
    public String updateProduct(@ModelAttribute ProductDTO productDTO,
                                @RequestParam MultipartFile file, // 수정: @RequestParam 추가
                                RedirectAttributes redirectAttributes) throws IOException {
        productService.update(productDTO, file);
        redirectAttributes.addFlashAttribute("message", "상품이 성공적으로 수정되었습니다.");
        return "redirect:/product/list";
    }
    //삭제(Get)
    @GetMapping("/product/delete")
    public String deleteProduct(@RequestParam Integer id, RedirectAttributes redirectAttributes) throws Exception {
        productService.delete(id);
        redirectAttributes.addFlashAttribute("message", "상품이 성공적으로 삭제되었습니다.");
        return "redirect:/product/list";
    }
    //개별조회(Get)
    // 상품 개별 조회
    @GetMapping("/product/view")
    public String getProductById(@RequestParam Integer id, Model model) {
        ProductDTO productDTO = productService.findById(id);
        model.addAttribute("productDTO", productDTO);
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);
        return "product/view";
    }
    //전체조회(Get)
    // 상품 전체 조회 (페이지 처리)
    @GetMapping("/product/list")
    public String listProducts(@RequestParam(value = "status", defaultValue = "OPEN") StatusType status,
                               Model model,
                               Pageable pageable) {
        Page<ProductDTO> productList = productService.findByStatus(status, pageable);
        model.addAttribute("list", productList);
        model.addAttribute("currentPage", productList.getNumber());
        model.addAttribute("totalPages", productList.getTotalPages());
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);
        model.addAttribute("selectedStatus", status);
        return "product/list"; // 상품 목록 페이지 경로
    }
}
