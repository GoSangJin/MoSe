package com.woori.studylogin.Repository;

import com.woori.studylogin.Constant.RegionType;
import com.woori.studylogin.Entity.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Integer> {
    Page<EventEntity> findByEventNameContaining(String eventName, Pageable pageable);
     // 지역별 검색 메서드 추가
    Page<EventEntity> findByRegionType(RegionType regionType, Pageable pageable);
}
