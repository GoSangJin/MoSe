package com.woori.studylogin.Repository;

import com.woori.studylogin.Entity.DocumentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, Integer> {
    // 공문 관련 데이터 조회를 위한 Repository
    Page<DocumentEntity> findByTitleContainingIgnoreCase(String title, Pageable pageable);

}
