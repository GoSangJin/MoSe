package com.woori.studylogin.Repository;

import com.woori.studylogin.Constant.CategoryType;
import com.woori.studylogin.Entity.BoardEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {
@Modifying

    @Query("update BoardEntity u Set u.viewCount = u.viewCount+1 Where u.id = :id")
    void viewcnt(@Param("id") Integer id);


    Page<BoardEntity> findByTitleContaining(String title, Pageable pageable);
    Page<BoardEntity> findByContentContaining(String content, Pageable pageable);
    Page<BoardEntity> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);
    Page<BoardEntity> findByCategory(CategoryType category, Pageable pageable); // 카테고리 필터링
    Page<BoardEntity> findByTitleContainingAndCategory(String title, CategoryType category, Pageable pageable); // 제목과 카테고리 필터링
    Page<BoardEntity> findByContentContainingAndCategory(String content, CategoryType category, Pageable pageable); // 내용과 카테고리 필터링
    @Query("SELECT b FROM BoardEntity b WHERE (b.title LIKE %:title% OR b.content LIKE %:content%) AND (:category IS NULL OR :category = '' OR b.category = :category) ORDER BY b.regDate DESC")
    Page<BoardEntity> findByTitleContainingOrContentContainingAndCategory(@Param("title") String title,
                                                                            @Param("content") String content,
                                                                            @Param("category") CategoryType category,
                                                                            Pageable pageable);
    // 공지사항만 가져오기 (정렬 추가)
    List<BoardEntity> findByCategory(CategoryType category, Sort sort);
    // 공지사항을 제외한 게시글 가져오기
    Page<BoardEntity> findByCategoryNot(CategoryType category, Pageable pageable);
    @Modifying
    @Transactional
    @Query("UPDATE BoardEntity b SET b.likeCount = b.likeCount + 1 WHERE b.id = :id")
    void increaseLikeCount(Integer id);

    // 추천 수 감소
    @Modifying
    @Transactional
    @Query("UPDATE BoardEntity b SET b.likeCount = b.likeCount - 1 WHERE b.id = :id")
    void decreaseLikeCount(Integer id);

    Page<BoardEntity> findByAuthor(Pageable pageable, String author);
}

