package com.woori.studylogin.Repository;

import com.woori.studylogin.Entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Integer> {
    // 특정 게시글과 사용자 조합으로 중복 신고를 확인하는 메서드
    Optional<ReportEntity> findById(Integer id);
    void deleteAllByBoardId(Integer boardId);
}