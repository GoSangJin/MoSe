package com.woori.studylogin.Service;

import com.woori.studylogin.DTO.DiseaseDTO;
import com.woori.studylogin.Entity.DiseaseEntity;
import com.woori.studylogin.Entity.FruitEntity;
import com.woori.studylogin.Repository.DiseaseRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Log
public class DiseaseService {
    @Value("${imgUploadLocation}")
    private String imgUploadLocation;
    private final FruitRepository fruitRepository;
    private final DiseaseRepository diseaseRepository;
    private final ModelMapper modelMapper;
    private final FileUpload fileUpload;

    public void create(DiseaseDTO diseaseDTO, MultipartFile[] files) throws IOException {
        List<String> fileNames = new ArrayList<>();
        Integer fruitId = diseaseDTO.getFruitId();

        if (fruitId == null) {
            throw new IllegalArgumentException("Fruit ID must not be null");
        }

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String newFileName = fileUpload.upload(file, imgUploadLocation);
                fileNames.add(newFileName);
            }
        }

        diseaseDTO.setDiseaseImg(fileNames);

        DiseaseEntity diseaseEntity = modelMapper.map(diseaseDTO, DiseaseEntity.class);

        if (fruitId != null) {
            FruitEntity fruitEntity = fruitRepository.findById(fruitId).orElseThrow();
            diseaseEntity.setFruit(fruitEntity);
        }

        diseaseRepository.save(diseaseEntity);
    }

    public void update(DiseaseDTO diseaseDTO, MultipartFile[] files) throws IOException {
        DiseaseEntity diseaseEntity = diseaseRepository.findById(diseaseDTO.getId()).orElseThrow();
        List<String> oldFileNames = diseaseEntity.getDiseaseImg();

        for (String oldFileName : oldFileNames) {
            fileUpload.deleteFile(oldFileName, imgUploadLocation);
        }

        List<String> newFileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String newFileName = fileUpload.upload(file, imgUploadLocation);
                newFileNames.add(newFileName);
            }
        }

        diseaseDTO.setDiseaseImg(newFileNames);

        DiseaseEntity updatedEntity = modelMapper.map(diseaseDTO, DiseaseEntity.class);

        if (diseaseDTO.getFruitId() != null) {
            FruitEntity fruitEntity = fruitRepository.findById(diseaseDTO.getFruitId()).orElseThrow();
            updatedEntity.setFruit(fruitEntity);
        }

        diseaseRepository.save(updatedEntity);
    }

    public void delete(Integer id) throws Exception {
        DiseaseEntity diseaseEntity = diseaseRepository.findById(id).orElseThrow();
        List<String> fileNames = diseaseEntity.getDiseaseImg();

        for (String fileName : fileNames) {
            fileUpload.deleteFile(fileName, imgUploadLocation);
        }

        diseaseRepository.deleteById(id);
    }

    public Page<DiseaseDTO> list(String searchType, String search, Pageable page) {
        int current = page.getPageNumber() - 1;
        int size = 12;
        Pageable pageable = PageRequest.of(current, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<DiseaseEntity> diseaseEntities;
        if (searchType.equals("name")) {
            diseaseEntities = diseaseRepository.findByDiseaseNameContaining(search, pageable);
        } else {
            diseaseEntities = diseaseRepository.findAll(pageable);
        }

        return diseaseEntities.map(data -> modelMapper.map(data, DiseaseDTO.class));
    }

    public DiseaseDTO read(Integer id) {
        DiseaseEntity diseaseEntity = diseaseRepository.findById(id).orElseThrow();
        return modelMapper.map(diseaseEntity, DiseaseDTO.class);
    }

    public List<DiseaseDTO> getDiseasesByFruitId(Integer fruitId) {
    List<DiseaseEntity> diseaseEntities = diseaseRepository.findByFruitId(fruitId);
    return diseaseEntities.stream()
        .map(diseaseEntity -> modelMapper.map(diseaseEntity, DiseaseDTO.class))
        .collect(Collectors.toList());
}

}
