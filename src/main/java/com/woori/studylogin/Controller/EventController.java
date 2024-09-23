package com.woori.studylogin.Controller;

import com.woori.studylogin.Constant.RegionType;
import com.woori.studylogin.DTO.EventDTO;
import com.woori.studylogin.Service.EventService;
import com.woori.studylogin.Util.FileUpload;
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

import java.io.IOException;
import java.util.List;
import java.util.Map;

/* Event // event >> Ctrl + R 로 변수명 변경 */
@Controller
@RequiredArgsConstructor
public class EventController {
    private final FileUpload fileUpload;
    @Value("kosangjin")
    public String bucket; //버킷명
    @Value("ap-northeast-2")
    public String region; //저장지역
    @Value("${imgUploadLocation}")
    public String folder; //버킷에 이용할 폴더명
    private String imgUploadLocation;
    private final EventService eventService;

    //삽입
    @GetMapping("/event/create")
    public String create(Model model) {
        model.addAttribute("eventDTO", new EventDTO());
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);
        return "event/create";
    }

    @PostMapping("/event/create")
    public String createProc(@ModelAttribute EventDTO eventDTO, Model model,
                             MultipartFile file) throws IOException {
        // URL과 이미지 URL을 동일하게 설정
    String eventUrl = eventDTO.getEventUrl(); // eventUrl에서 URL 값을 가져옴
    eventDTO.setEventImg(eventUrl); // 이미지 URL을 URL 값으로 설정
    // 파일이 업로드된 경우
    if (!file.isEmpty()) {
        String oriFileName = file.getOriginalFilename();
        String newFileName = fileUpload.upload(file, imgUploadLocation);
        eventDTO.setEventImg(newFileName);
    }
        eventService.create(eventDTO, file);
        return "redirect:/event";
    }

    //수정
    @GetMapping("/event/update")
    public String update(@RequestParam Integer id, Model model) {

        EventDTO eventDTO = eventService.read(id);

        model.addAttribute("eventDTO", eventDTO);
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);
        return "event/update";
    }

    @PostMapping("/event/update")
    public String updateProc(EventDTO eventDTO, MultipartFile file)throws IOException {
        eventService.update(eventDTO,file);
        return "redirect:/event";
    }

    //삭제
    @GetMapping("/event/delete")
    public String delete(@RequestParam Integer id) throws IOException{
        eventService.delete(id);
        return "redirect:/event";
    }

    //상세조회
    @GetMapping("/event/read")
    public String read(Integer id, Model model) {
        EventDTO eventDTO = eventService.read(id);
        model.addAttribute("eventDTO", eventDTO);
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);
        return "event/read";
    }

    //목록
    // 전체조회(Get)
    @GetMapping("/event")
public String getAllEvents(@PageableDefault(page = 1) Pageable pageable,
                           @RequestParam(required = false) String regionType,
                           @RequestParam(value = "searchType", defaultValue = "") String searchType,
                           @RequestParam(value = "search", defaultValue = "") String search,
                           Model model) {

    model.addAttribute("regionTypes", RegionType.values()); // 지역 타입 리스트 추가
    // regionType이 "all"인 경우 모든 이벤트를 가져옵니다.
    if (regionType == null || "all".equals(regionType)) {
        regionType = null; // 모든 이벤트를 가져오도록 null로 설정
    }
    Page<EventDTO> eventPage = eventService.list(searchType, search, regionType, pageable);
    List<EventDTO> eventDTOList = eventPage.getContent();
    model.addAttribute("eventDTOList", eventDTOList);
    model.addAttribute("selectedRegion", regionType);
    model.addAttribute("bucket", bucket);
    model.addAttribute("region", region);
    model.addAttribute("folder", folder);
    System.out.println("Selected region: " + regionType);
    Map<String, Integer> pageInfo = PaginationUtil.Pagination(eventPage);
    model.addAllAttributes(pageInfo);

    model.addAttribute("searchType", searchType);
    model.addAttribute("search", search);

    return "event/list";
}

}