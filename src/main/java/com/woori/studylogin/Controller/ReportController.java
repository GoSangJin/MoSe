package com.woori.studylogin.Controller;

import com.woori.studylogin.DTO.ReportDTO;
import com.woori.studylogin.Service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
public class ReportController  {

    @Autowired
    private ReportService reportService;


    @GetMapping("/board/report")
    public String showReportForm(@RequestParam Integer boardId, Model model) {
        model.addAttribute("boardId", boardId);
        return "/board/report";
    }

    @PostMapping("/report")
public String reportPost(@RequestParam Integer boardId, @RequestParam String title,
                         @RequestParam String description, @RequestParam String username) {
    System.out.println("Received username: " + username); // 디버깅용 로그
    try {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setTitle(title);
        reportDTO.setDescription(description);
        reportDTO.setBoardId(boardId);
        reportDTO.setReporterUsername(username);
        reportService.saveReport(reportDTO);
        return "redirect:/board/report?boardId=" + boardId + "&success=true";
    } catch (Exception e) {
        return "redirect:/board/report?boardId=" + boardId + "&error=" + e.getMessage();
    }
}

    @GetMapping("/admin/report")
    public String viewReport(Model model) {
        List<ReportDTO> reports = reportService.getAllReports();
        model.addAttribute("reports", reports);
        return "/admin/report";
    }

    @GetMapping("/admin/report_content")
    public String viewReportedPosts(@RequestParam Integer id, Model model) {
        ReportDTO reports = reportService.getReportById(id);

        List<ReportDTO> reportList = reportService.getAllReports();

        model.addAttribute("reportList", reportList);

        model.addAttribute("reports", reports);

        return "/admin/report_content";
    }

    @PostMapping("/admin/handleReport")
    public String handleReport(@RequestParam("reportId") String reportIdStr,
                           @RequestParam("suspensionType") String suspensionType)  throws IOException {
    Integer reportId = null;
    try {
        reportId = Integer.valueOf(reportIdStr);
    } catch (NumberFormatException e) {
        // 오류 처리 로직
        e.printStackTrace();
    }

    reportService.handleReport(reportId, suspensionType);
    // reportId와 suspensionType을 사용해 신고 처리

    return "redirect:/admin/report";
}



}