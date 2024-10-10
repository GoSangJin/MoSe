package com.woori.studylogin.Repository;

import com.woori.studylogin.Entity.QnaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QnaRepository extends JpaRepository<QnaEntity, Integer> {
    // JpaRepository 인터페이스를 통해 기본적인 CRUD 기능을 사용할 수 있습니다.
    Page<QnaEntity> findByTitleContaining(String title, Pageable pageable);
}
