package com.woori.studylogin.Service;

import com.woori.studylogin.DTO.DiseaseDTO;
import com.woori.studylogin.DTO.FruitDTO;
import com.woori.studylogin.Entity.FruitEntity;
import com.woori.studylogin.Repository.FruitRepository;
import com.woori.studylogin.Util.FileUpload;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Log
public class FruitService {
    @Value("${imgUploadLocation}")
    private String imgUploadLocation;
    private final FruitRepository fruitRepository;
    private final DiseaseService diseaseService; // DiseaseService를 주입하여 병해 정보를 조회
    private final ModelMapper modelMapper;
    private final FileUpload fileUpload;

    public void create(FruitDTO fruitDTO, MultipartFile file) throws IOException {
        String fileName = "";
        if (!file.isEmpty()) {
            fileName = fileUpload.upload(file, imgUploadLocation);
        }

        fruitDTO.setFruitImg(fileName);

        FruitEntity fruitEntity = modelMapper.map(fruitDTO, FruitEntity.class);
        fruitRepository.save(fruitEntity);
    }

    public void update(FruitDTO fruitDTO, MultipartFile file) throws IOException {
        FruitEntity fruitEntity = fruitRepository.findById(fruitDTO.getId()).orElseThrow();

        fileUpload.deleteFile(fruitEntity.getFruitImg(), imgUploadLocation);

        String newFileName = "";
        if (!file.isEmpty()) {
            newFileName = fileUpload.upload(file, imgUploadLocation);
        }

        fruitDTO.setFruitImg(newFileName);

        FruitEntity updatedEntity = modelMapper.map(fruitDTO, FruitEntity.class);
        fruitRepository.save(updatedEntity);
    }

    public void delete(Integer id) throws Exception {
        FruitEntity fruitEntity = fruitRepository.findById(id).orElseThrow();
        fileUpload.deleteFile(fruitEntity.getFruitImg(), imgUploadLocation);
        fruitRepository.deleteById(id);
    }

    public Page<FruitDTO> list(String searchType, String search, Pageable page) {
        int current = page.getPageNumber() - 1;
        int size = 12;
        Pageable pageable = PageRequest.of(current, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<FruitEntity> fruitEntities;
        // 검색 타입에 따른 필터링 처리
    if (searchType.equals("name") && !search.isEmpty()) {
        // 검색어가 있는 경우 해당 검색어를 포함하는 작물 이름으로 필터링
        fruitEntities = fruitRepository.findByFruitNameContaining(search, pageable);
    } else {
        // 검색어가 없거나 다른 타입일 경우 전체 조회
        fruitEntities = fruitRepository.findAll(pageable);
    }

    // FruitEntity에서 FruitDTO로 변환
    return fruitEntities.map(fruitEntity -> {
        FruitDTO fruitDTO = modelMapper.map(fruitEntity, FruitDTO.class);
        // 작물에 연결된 병해 정보 리스트 가져오기
        List<DiseaseDTO> diseaseDTOList = diseaseService.getDiseasesByFruitId(fruitEntity.getId());
        fruitDTO.setDiseaseDTOList(diseaseDTOList);
        return fruitDTO;
    });
    }

    public FruitDTO read(Integer id) {
        FruitEntity fruitEntity = fruitRepository.findById(id).orElseThrow();
        return modelMapper.map(fruitEntity, FruitDTO.class);
    }
}

