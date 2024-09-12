package com.woori.studylogin.Controller;

import com.woori.studylogin.DTO.EventDTO;
import com.woori.studylogin.Service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/* Event // event >> Ctrl + R 로 변수명 변경 */
@Controller
@RequiredArgsConstructor
public class EventController {
    @Value("kosangjin")
    public String bucket; //버킷명
    @Value("ap-northeast-2")
    public String region; //저장지역
    @Value("${imgUploadLocation}")
    public String folder; //버킷에 이용할 폴더명

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
    @GetMapping("/event")
    public String list(Model model) {
        List<EventDTO> eventDTOList = eventService.list();
        model.addAttribute("eventDTOList", eventDTOList);
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);

        return "event/list";
    }

}