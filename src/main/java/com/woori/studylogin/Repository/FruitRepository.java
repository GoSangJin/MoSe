package com.woori.studylogin.Repository;

import com.woori.studylogin.Entity.FruitEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FruitRepository extends JpaRepository<FruitEntity, Integer> {
    Page<FruitEntity> findByFruitNameContaining(String fruitName, Pageable pageable);
    Optional<FruitEntity> findById(Integer id);
}
