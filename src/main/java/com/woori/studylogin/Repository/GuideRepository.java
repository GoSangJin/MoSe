package com.woori.studylogin.Repository;

import com.woori.studylogin.Entity.GuideEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuideRepository extends JpaRepository<GuideEntity, Integer> {
}

