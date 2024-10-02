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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    // 삽입
    @PostMapping("/event/create")
    public String createProc(@ModelAttribute EventDTO eventDTO, Model model) throws IOException {
        // URL과 이미지 URL을 동일하게 설정
        String eventUrl = eventDTO.getEventUrl(); // eventUrl에서 URL 값을 가져옴
        eventDTO.setEventImg(eventUrl); // 이미지 URL을 URL 값으로 설정

        eventService.create(eventDTO, null);
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
    public String updateProc(EventDTO eventDTO) throws IOException {
        // 기존 이벤트 정보 가져오기
        EventDTO existingEvent = eventService.read(eventDTO.getId());

        // URL과 이미지 URL을 동일하게 설정 (업데이트 시 새 URL 사용)
    if (eventDTO.getEventUrl() != null && !eventDTO.getEventUrl().isEmpty()) {
        // 새로운 URL로 이미지 URL 설정
        eventDTO.setEventImg(eventDTO.getEventUrl());
    } else {
        // 기존 이미지 URL 유지
        eventDTO.setEventImg(existingEvent.getEventImg());
    }

        eventService.update(eventDTO, null);
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

        // DEFAULT를 제외한 RegionType 리스트 생성
        List<RegionType> filteredRegionTypes = Arrays.stream(RegionType.values())
                .filter(region -> !region.equals(RegionType.DEFAULT))
                .collect(Collectors.toList());

        model.addAttribute("regionTypes", filteredRegionTypes); // 지역 타입 리스트 추가

        // "all"인 경우 또는 null인 경우 전체 조회로 처리
        RegionType selectedRegion = null;
        if (regionType != null && !"all".equalsIgnoreCase(regionType)) {
            try {
                selectedRegion = RegionType.valueOf(regionType);
            } catch (IllegalArgumentException e) {
                // 유효하지 않은 값인 경우 기본값(null)으로 유지
                System.out.println("Invalid region type: " + regionType);
            }
        }

        Page<EventDTO> eventPage = eventService.list(searchType, search, selectedRegion != null ? selectedRegion.name() : null, pageable);
        List<EventDTO> eventDTOList = eventPage.getContent();

        model.addAttribute("eventDTOList", eventDTOList);
        model.addAttribute("selectedRegion", selectedRegion != null ? selectedRegion.name() : "all");
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