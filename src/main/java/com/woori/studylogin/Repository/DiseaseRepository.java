package com.woori.studylogin.Repository;

import com.woori.studylogin.Entity.DiseaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiseaseRepository extends JpaRepository<DiseaseEntity, Integer> {
    Page<DiseaseEntity> findByDiseaseNameContaining(String DiseaseName, Pageable pageable);
    Optional<DiseaseEntity> findById(Integer id);
    List<DiseaseEntity> findByFruitId(Integer fruitId);
}
