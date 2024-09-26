package com.woori.studylogin.Repository;

import com.woori.studylogin.Constant.RegionType;
import com.woori.studylogin.Constant.StatusType;
import com.woori.studylogin.Entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    Page<ProductEntity> findByStatus(StatusType status, Pageable pageable);
    Page<ProductEntity> findByRegionType(RegionType regionType, Pageable pageable);

}


