package com.woori.studylogin.Service;

import com.woori.studylogin.Constant.RegionType;
import com.woori.studylogin.Constant.StatusType;
import com.woori.studylogin.DTO.ProductDTO;
import com.woori.studylogin.Entity.ProductEntity;
import com.woori.studylogin.Repository.ProductRepository;
import com.woori.studylogin.Util.FileUpload;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ProductService {
    @Value("${imgUploadLocation}") //작업값이 하나만 존재할 때는 application에 저장해서 호출해서 이용
    private String imgUploadLocation;

    @Autowired
    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;
    private final FileUpload fileUpload;


    //삽입(삽입처리->파일추가)
    public void save(ProductDTO productDTO, MultipartFile file) throws IOException {
        String oriFileName = file.getOriginalFilename();
        String newFileName = "";

        if (oriFileName != null) {
            newFileName = fileUpload.upload(file, imgUploadLocation);
        }
        productDTO.setImage_url(newFileName); // DTO의 이미지 URL 설정

        //상품정보
        ProductEntity productEntity = modelMapper.map(productDTO, ProductEntity.class);
        productRepository.save(productEntity);
    }

    //수정(수정->파일추가)
    public void update(ProductDTO productDTO, MultipartFile file) throws IOException {
        //유효성검사(Save작업전 데이터베이스 존재여부 확인)
        ProductEntity productEntity = productRepository.findById(productDTO.getId()).orElseThrow(() ->
                new IllegalArgumentException("No product found with id: " + productDTO.getId()));

        String deleteFile = productEntity.getImage_url(); //현재저장된 이미지파일명

        //MultipartFile 작업(물리적 저장)
        String oriFileName = file.getOriginalFilename(); //업로드된 파일명을 읽기
        String newFileName = ""; //S3에 저장된 파일명

        if (oriFileName != null) { //유효성검사(작업이 가능한지 확인)-작업할 파일이 있으면
            //기존파일을 삭제
            if (deleteFile.length() != 0) { //이전에 저장된 파일이 존재하면
                fileUpload.deleteFile(deleteFile, imgUploadLocation);
            }
            newFileName = fileUpload.upload(file, imgUploadLocation);
            productDTO.setImage_url(newFileName); //데이터베이스에 상품이미지이름을 추가
        }

        //값을 변환해서 저장(DTO에 대한 작업)
        ProductEntity data = modelMapper.map(productDTO, ProductEntity.class);
        productRepository.save(data);
    }

    //삭제
    public void delete(Integer id) throws Exception {
        //기존에 저장된 이미지가 있으면 파일삭제 추가
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("No product found with id: " + id)); //대상존재 여부(유효성 검사)

        //현재 레코드의 이미지파일을 삭제
        fileUpload.deleteFile(productEntity.getImage_url(), imgUploadLocation);
        //레코드 삭제
        productRepository.deleteById(id);
    }

    //개별조회
    public ProductDTO findById(Integer id) {
        Optional<ProductEntity> read = productRepository.findById(id);
        if (read.isPresent()) {
            return modelMapper.map(read.get(), ProductDTO.class); // Optional에서 값 꺼내오기
        } else {
            throw new IllegalArgumentException("No product found with id: " + id);
        }
    }

    //전체조회
    public Page<ProductDTO> findAll(Pageable page) {
        // PageNumber가 0부터 시작하므로 -1을 하지 않음
        int current = page.getPageNumber();
        int size = 12; // 페이지 당 데이터 수
        Pageable pageable = PageRequest.of(current, size, Sort.by(Sort.Direction.DESC, "id")); // 페이지 요청 생성
        Page<ProductEntity> productEntities = productRepository.findAll(pageable); // 데이터 조회

        // ProductEntity를 ProductDTO로 매핑하여 반환
        return productEntities.map(productEntity -> modelMapper.map(productEntity, ProductDTO.class));
    }

    public Page<ProductDTO> findByStatus(StatusType status, Pageable pageable) {
        // StatusType을 사용하여 상품 조회
        Page<ProductEntity> productEntities = productRepository.findByStatus(status, pageable);
        return productEntities.map(productEntity -> modelMapper.map(productEntity, ProductDTO.class));
    }

    public Page<ProductDTO> findByRegionType(RegionType regionType, Pageable pageable) {
        Page<ProductEntity> productEntities = productRepository.findByRegionType(regionType, pageable);
        return productEntities.map(productEntity -> modelMapper.map(productEntity, ProductDTO.class));
    }




}
