package com.woori.studylogin.Repository;

import com.woori.studylogin.Entity.FruitEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FruitRepository extends JpaRepository<FruitEntity, Integer> {
    Page<FruitEntity> findByFruitNameContaining(String fruitName, Pageable pageable);
    Page<FruitEntity> findByFruitNameContainingIgnoreCase(String fruitName, Pageable pageable);
}

