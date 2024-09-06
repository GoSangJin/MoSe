package com.woori.studylogin.Controller;

import com.woori.studylogin.Constant.StatusType;
import com.woori.studylogin.DTO.ProductDTO;
import com.woori.studylogin.Service.ProductService;
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
        model.addAttribute("statusTypes", StatusType.values()); // 모든 StatusType을 모델에 추가
        model.addAttribute("selectedStatus", StatusType.OPEN); // 현재 상품의 상태
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);
        return "/product/insert";
    }

    // 상품 생성 처리
    @PostMapping("/product/new")
    public String createProduct(@ModelAttribute ProductDTO productDTO,
                              MultipartFile file,
                              RedirectAttributes redirectAttributes) throws IOException {

        productService.save(productDTO, file);

        redirectAttributes.addFlashAttribute("message", "상품이 성공적으로 생성되었습니다.");
        return "redirect:/product/list";
    }
    //수정(Get, Post)
    // 상품 수정 폼
    @GetMapping("/product/edit")
    public String showUpdateForm(@RequestParam Integer id, Model model) {
        ProductDTO productDTO = productService.findById(id);

        // 모델에 상태와 관련된 정보를 추가
        model.addAttribute("statusTypes", StatusType.values()); // 모든 StatusType을 모델에 추가
        model.addAttribute("selectedStatus", productDTO.getStatus()); // 현재 상품의 상태
        model.addAttribute("productDTO", productDTO);
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);

        return "product/update";
    }
    
    // 상품 수정 처리
    @PostMapping("/product/edit")
    public String updateProduct(@ModelAttribute ProductDTO productDTO,
                                MultipartFile file,
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
    public String getAllProducts(@PageableDefault(page=1) Pageable pageable,
                               Model model) {
        Page<ProductDTO> productPage = productService.findAll(pageable);
        model.addAttribute("list", productPage);
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);
        Map<String, Integer> pageInfo = PaginationUtil.Pagination(productPage);
        model.addAllAttributes(pageInfo);

        return "product/list";
    }
    //@PathVariable : 변수가 매핑에 있는 경우 @GetMapping("/product/{id}")
    //@RequestParam : 전달된 변수가 있는 경우 http://localhost:800/product?id=1
    //@ModelAttribute : DTO에 변수가 있는 경우
    //@PageableDefault : 페이지정보가 있는 경우(페이지정보가 없으면 기본값)
    //@RequestParam, @ModelAttribute는 변수명이 동일하면 생략가능
    //@PathVariable, @PageableDefault는 반드시 선언 사용
    //@PathVariable는 RestController 사용시
}
// 상품등록/수정/삭제-로그인시에만 노출
//            상세보기-모두 노출
//브라우저 요청을 받아서 브라우저에 응답
//GetMapping, PostMapping에 이름지정
//return을 통해 이동하려는 페이지 지정

//HTML에서 DTO를 받아서 데이터베이스에 처리하도록 서비스에 전달
//서비스에서 전달받은 DTO를 HTML에 전달