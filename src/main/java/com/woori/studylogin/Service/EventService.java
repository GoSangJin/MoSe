package com.woori.studylogin.Service;


import com.woori.studylogin.Constant.RegionType;
import com.woori.studylogin.DTO.EventDTO;
import com.woori.studylogin.Entity.EventEntity;
import com.woori.studylogin.Repository.EventRepository;
import com.woori.studylogin.Util.FileUpload;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class EventService {

    @Value("${imgUploadLocation}")
    private String imgUploadLocation;
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final FileUpload fileUpload;

    // 삽입
    public void create(EventDTO eventDTO, MultipartFile file) throws IOException {
        String oriFileName = file.getOriginalFilename();
        String newFileName = "";

        // 파일이 null이 아니고 비어있지 않을 경우
        if (oriFileName != null && !oriFileName.isEmpty()) {
        newFileName = fileUpload.upload(file, imgUploadLocation);
        eventDTO.setEventImg(newFileName);
    }

    EventEntity eventEntity = modelMapper.map(eventDTO, EventEntity.class);
    eventRepository.save(eventEntity);
    }

    // 수정
    public void update(EventDTO eventDTO, MultipartFile file) throws IOException {
        EventEntity eventEntity = eventRepository.findById(eventDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        String deleteFile = eventEntity.getEventImg();
        String newFileName = "";

        // 파일이 null이 아니고 비어있지 않을 경우
        if (file != null && !file.isEmpty()) {
            if (deleteFile != null && !deleteFile.isEmpty()) {
                fileUpload.deleteFile(deleteFile, imgUploadLocation);
            }
            newFileName = fileUpload.upload(file, imgUploadLocation);
            eventDTO.setEventImg(newFileName);
        } else {
            // 파일이 null이거나 비어있을 경우 기존 이미지를 유지
            eventDTO.setEventImg(deleteFile);
        }

        EventEntity data = modelMapper.map(eventDTO, EventEntity.class);
        eventRepository.save(data);
    }

    // 삭제
    public void delete(Integer id) throws IOException {
        EventEntity eventEntity = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        fileUpload.deleteFile(eventEntity.getEventImg(), imgUploadLocation);
        eventRepository.deleteById(id);
    }

    // 상세보기
    public EventDTO read(Integer id) {
        EventEntity eventEntity = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        return modelMapper.map(eventEntity, EventDTO.class);
    }

    // 리스트
    public Page<EventDTO> list(String searchType, String search, String regionType, Pageable page) {
    int current = page.getPageNumber() - 1;
    int size = 12;
    Pageable pageable = PageRequest.of(current, size, Sort.by(Sort.Direction.DESC, "id"));

    Page<EventEntity> eventEntities;

    // 지역 타입에 따른 검색 처리
    if (regionType != null && !regionType.isEmpty()) {
        RegionType region = RegionType.valueOf(regionType);
        eventEntities = eventRepository.findByRegionType(region, pageable);
    } else if (searchType.equals("eventName") && search != null && !search.isEmpty()) {
        eventEntities = eventRepository.findByEventNameContaining(search, pageable);
    } else {
        eventEntities = eventRepository.findAll(pageable);
    }

    return eventEntities.map(data -> modelMapper.map(data, EventDTO.class));
}
    public Page<EventDTO> findByRegionType(RegionType regionType, Pageable pageable) {
    Page<EventEntity> eventEntities = eventRepository.findByRegionType(regionType, pageable);
    return eventEntities.map(data -> modelMapper.map(data, EventDTO.class));
}

}
