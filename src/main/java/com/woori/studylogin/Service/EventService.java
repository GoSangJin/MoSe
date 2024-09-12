package com.woori.studylogin.Service;


import com.woori.studylogin.DTO.EventDTO;
import com.woori.studylogin.Entity.EventEntity;
import com.woori.studylogin.Repository.EventRepository;
import com.woori.studylogin.Util.FileUpload;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/* Event // event >> Ctrl + R 로 변수명 변경 */
@Service
@Transactional
@RequiredArgsConstructor
public class EventService {
    @Value("${imgUploadLocation}") //작업값이 하나만 존재할 때는 application에 저장해서 호출해서 이용
    private String imgUploadLocation;
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final FileUpload fileUpload;

    //삽입
    public void create(EventDTO eventDTO, MultipartFile file)throws IOException {
        String oriFileName = file.getOriginalFilename();
        String newFileName = "";

        if(oriFileName != null) {
            newFileName = fileUpload.upload(file, imgUploadLocation);
        }
        eventDTO.setEventImg(newFileName);

        EventEntity eventEntity = modelMapper.map(eventDTO, EventEntity.class);
        eventRepository.save(eventEntity);
    }

    //수정
    public void update(EventDTO eventDTO, MultipartFile file)throws IOException {
        EventEntity eventEntity = eventRepository.findById(eventDTO.getId()).orElseThrow();
        String deleteFile = eventEntity.getEventImg();

         //MultipartFile 작업(물리적 저장)
        String oriFileName = file.getOriginalFilename(); //업로드된 파일명을 읽기
        String newFileName = ""; //S3에 저장된 파일명

        if(oriFileName != null) {
            if(deleteFile.length() != 0 ){
                fileUpload.deleteFile(deleteFile, imgUploadLocation);
            }
            newFileName = fileUpload.upload(file, imgUploadLocation);
            eventDTO.setEventImg(newFileName);
        }
        EventEntity data = modelMapper.map(eventDTO, EventEntity.class);
        eventRepository.save(data);
    }

    //삭제
    public void delete(Integer id) throws IOException {
        EventEntity eventEntity = eventRepository.findById(id).orElseThrow();

        fileUpload.deleteFile(eventEntity.getEventImg(), imgUploadLocation);

        eventRepository.deleteById(id);
    }

    //상세보기
    public EventDTO read(Integer id) {
        Optional<EventEntity> eventEntity = eventRepository.findById(id);
        EventDTO eventDTO = modelMapper.map(eventEntity, EventDTO.class);
        return eventDTO;
    }

    //리스트
    public List<EventDTO> list() {
        List<EventEntity> eventEntities = eventRepository.findAll();
        List<EventDTO> eventDTOS = Arrays.asList(modelMapper.map(eventEntities, EventDTO[].class));
        return eventDTOS;
    }
}
