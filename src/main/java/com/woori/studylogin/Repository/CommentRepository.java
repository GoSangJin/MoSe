package com.woori.studylogin.Repository;

import com.woori.studylogin.Entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
@Query(value="SELECT * FROM comment WHERE boardid =:boardid", nativeQuery = true)
    List<CommentEntity> findByComment(Integer boardid);
}
