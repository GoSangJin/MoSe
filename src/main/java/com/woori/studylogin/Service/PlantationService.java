package com.woori.studylogin.Service;

import com.woori.studylogin.Constant.PlantType;
import com.woori.studylogin.DTO.PlantationDTO;
import com.woori.studylogin.Entity.PlantationEntity;
import com.woori.studylogin.Repository.PlantationRepository;
import com.woori.studylogin.Util.FileUpload;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

//Entity, Repository->Service(복잡생성)
@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class PlantationService {
    @Value("${imgUploadLocation}") //작업값이 하나만 존재할 때는 application에 저장해서 호출해서 이용
    private String imgUploadLocation;

    private final PlantationRepository plantationRepository;
    private final ModelMapper modelMapper;
    private final FileUpload fileUpload;

    //삽입(삽입처리->파일추가)
    public void save(PlantationDTO plantationDTO, MultipartFile file) throws IOException {
        plantationDTO.setEfficacy(plantationDTO.getEfficacy().replace("\n", "<br>"));
        plantationDTO.setCultivation(plantationDTO.getCultivation().replace("\n", "<br>"));
        plantationDTO.setCharacteristics(plantationDTO.getCharacteristics().replace("\n", "<br>"));
        plantationDTO.setPrecautions(plantationDTO.getPrecautions().replace("\n", "<br>"));
        plantationDTO.setRegion(plantationDTO.getRegion().replace("\n", "<br>"));
        plantationDTO.setHarvest(plantationDTO.getHarvest().replace("\n", "<br>"));

        String oriFileName = file.getOriginalFilename();
        String newFileName = "";

        if(oriFileName != null) {
            newFileName = fileUpload.upload(file, imgUploadLocation);
        }
        plantationDTO.setThumbnail_img(newFileName);

        //상품정보
        PlantationEntity plantationEntity = modelMapper.map(plantationDTO, PlantationEntity.class);
        plantationRepository.save(plantationEntity);
    }

    public void update(PlantationDTO plantationDTO, MultipartFile file) throws IOException {
        plantationDTO.setEfficacy(plantationDTO.getEfficacy().replace("\n", "<br>"));
        plantationDTO.setCultivation(plantationDTO.getCultivation().replace("\n", "<br>"));
        plantationDTO.setCharacteristics(plantationDTO.getCharacteristics().replace("\n", "<br>"));
        plantationDTO.setPrecautions(plantationDTO.getPrecautions().replace("\n", "<br>"));
        plantationDTO.setRegion(plantationDTO.getRegion().replace("\n", "<br>"));
        plantationDTO.setHarvest(plantationDTO.getHarvest().replace("\n", "<br>"));


        // 유효성 검사 (Save 작업 전 데이터베이스 존재 여부 확인)
    PlantationEntity plantationEntity = plantationRepository.findById(plantationDTO.getId())
            .orElseThrow(() -> new IllegalStateException("존재하지 않는 상품입니다."));

    String existingFileName = plantationEntity.getThumbnail_img(); // 현재 저장된 이미지 파일명
    String newFileName = ""; // S3에 저장된 파일명

    // MultipartFile 작업 (물리적 저장)
    if (file != null && !file.isEmpty()) { // 유효성 검사 (작업할 파일이 있으면)
        // 기존 파일을 삭제
        if (existingFileName != null && !existingFileName.isEmpty()) { // 이전에 저장된 파일이 존재하면
            fileUpload.deleteFile(existingFileName, imgUploadLocation);
        }
        newFileName = fileUpload.upload(file, imgUploadLocation); // 새 파일 업로드
        plantationDTO.setThumbnail_img(newFileName); // 데이터베이스에 새로운 이미지 이름을 설정
    } else {
        // 파일이 없을 경우 기존 이미지 유지
        plantationDTO.setThumbnail_img(existingFileName);
    }

    // 값 변환 및 저장 (DTO에 대한 작업)
    PlantationEntity data = modelMapper.map(plantationDTO, PlantationEntity.class);
    plantationRepository.save(data);
}

    //삭제
    public void delete(Integer id) throws Exception {
        //기존에 저장된 이미지가 있으면 파일삭제 추가
        PlantationEntity plantationEntity = plantationRepository.findById(id).orElseThrow(); //대상존재 여부(유효성 검사)
        //현재 레코드의 이미지파일을 삭제
        fileUpload.deleteFile(plantationEntity.getThumbnail_img(), imgUploadLocation);
        //레코드 삭제
        plantationRepository.deleteById(id);
    }

    //개별조회
    public PlantationDTO findById(Integer id) {
        Optional<PlantationEntity> read = plantationRepository.findById(id);
        if (read.isPresent()) {
            return modelMapper.map(read, PlantationDTO.class);
        } else {
            throw new IllegalArgumentException("No plantation found with id: " + id);
        }
    }

    //전체조회
    public Page<PlantationDTO> findAll(Pageable page, String search) {
        // 페이지 번호를 0부터 시작하도록 설정
        int current = page.getPageNumber();
        int size = 12; // 페이지당 아이템 수
        Pageable pageable = PageRequest.of(current, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<PlantationEntity> plantationEntities;

        // 검색어가 비어있지 않은 경우
        if (search != null && !search.isEmpty()) {
            plantationEntities = plantationRepository.findByTitleContaining(search, pageable);
        } else {
            // 데이터 조회
            plantationEntities = plantationRepository.findAll(pageable);
        }

        // DTO로 변환 후 반환
        return plantationEntities.map(plantationEntity -> modelMapper.map(plantationEntity, PlantationDTO.class));
    }

    public Page<PlantationDTO> findByStatus(PlantType status, Pageable pageable) {
        Page<PlantationEntity> plantationEntities = plantationRepository.findByStatus(status, pageable);
        return plantationEntities.map(plantationEntity -> modelMapper.map(plantationEntity, PlantationDTO.class));
    }

}