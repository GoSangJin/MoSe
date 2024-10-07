package com.woori.studylogin.Controller;

import com.woori.studylogin.Constant.RoleType;
import com.woori.studylogin.Service.AdminService;
import com.woori.studylogin.Service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private BoardService boardService;

    @GetMapping("/admin/user_info")
    public String listUsers(
            @RequestParam(name = "roleType", defaultValue = "") String roleType,
            @RequestParam(name = "searchType", defaultValue = "all") String searchType,
            @RequestParam(name = "searchValue", defaultValue = "") String searchValue,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        // 사용자 목록 조회
        Map<String, Object> result = adminService.getUserList(searchType, searchValue, roleType, page);

        model.addAttribute("currentPage", "userInfo");
        model.addAttribute("userDTOS", result.get("userDTOS"));
        model.addAttribute("pageInfo", result.get("pageInfo"));
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("roleType", roleType); // 역할 타입 추가

        return "admin/user_info";
    }

    @PostMapping("/admin/user_info/update_role")
    public String updateRole(@RequestParam Integer userId, @RequestParam RoleType roleType) {
        try {
            adminService.updateUserRole(userId, roleType);
        } catch (RuntimeException e) {
            // 로그를 남기거나 에러 메시지를 처리할 수 있습니다.
            return "redirect:/admin/user_info?error=update_failed"; // 에러 발생 시 리디렉션
        }
        return "redirect:/admin/user_info"; // 성공적으로 업데이트 후 리디렉션
    }

    @PostMapping("/admin/user_info/delete")
    public String deleteUser(@RequestParam("userId") Integer userId) {
        try {
            adminService.deleteUser(userId);
        } catch (RuntimeException e) {
            // 로그를 남기거나 에러 메시지를 처리할 수 있습니다.
            return "redirect:/admin/user_info?error=delete_failed"; // 에러 발생 시 리디렉션
        }
        return "redirect:/admin/user_info"; // 성공적으로 삭제 후 리디렉션
    }
}
