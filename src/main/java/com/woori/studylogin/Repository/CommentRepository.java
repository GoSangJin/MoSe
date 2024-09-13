package com.woori.studylogin.Repository;

import com.woori.studylogin.Entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    @Query(value="SELECT * FROM comment WHERE board_id =:boardid", nativeQuery = true)
    Page<CommentEntity> findByComment(Integer boardid, Pageable pageable);
// 사용자가 작성한 댓글 가져오기
    @Query("SELECT c FROM CommentEntity c WHERE c.user.username = :username")
    Page<CommentEntity> findByUser(@Param("username") String username, Pageable pageable);
}

