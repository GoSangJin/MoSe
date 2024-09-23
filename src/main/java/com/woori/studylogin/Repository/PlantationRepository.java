package com.woori.studylogin.Repository;

import com.woori.studylogin.Constant.PlantType;
import com.woori.studylogin.Entity.PlantationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantationRepository extends JpaRepository<PlantationEntity, Integer> {
    Page<PlantationEntity> findByTitleContaining(String title, Pageable pageable);
    Page<PlantationEntity> findByStatus(PlantType status, Pageable pageable);
}