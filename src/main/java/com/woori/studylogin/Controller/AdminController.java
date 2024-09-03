package com.woori.studylogin.Controller;

import com.woori.studylogin.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/admin/user_info")
    public String listUsers(
            @RequestParam(name = "searchName", defaultValue = "") String searchName,
            @RequestParam(name = "searchBirth", defaultValue = "") String searchBirth,
            @RequestParam(name = "searchAddress", defaultValue = "") String searchAddress,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        Map<String, Object> result = adminService.getUserList(searchName, searchBirth, searchAddress, page);

        model.addAttribute("userDTOS", result.get("userDTOS"));
        model.addAttribute("pageInfo", result.get("pageInfo"));
        model.addAttribute("searchName", result.get("searchName"));
        model.addAttribute("searchBirth", result.get("searchBirth"));
        model.addAttribute("searchAddress", result.get("searchAddress"));

        return "admin/user_info";
    }
}
