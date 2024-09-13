package com.woori.studylogin.Service;

import com.woori.studylogin.Constant.CategoryType;
import com.woori.studylogin.DTO.BoardDTO;
import com.woori.studylogin.Entity.BoardEntity;
import com.woori.studylogin.Entity.LikeEntity;
import com.woori.studylogin.Entity.UserEntity;
import com.woori.studylogin.Repository.BoardRepository;
import com.woori.studylogin.Repository.CommentRepository;
import com.woori.studylogin.Repository.LikeRepository;
import com.woori.studylogin.Repository.UserRepository;
import com.woori.studylogin.Util.FileUpload;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class BoardService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;
    private final FileUpload fileUpload;

    @Value("${imgUploadLocation}")
    private String imgUploadLocation;

    // 삽입
    public void save(BoardDTO boardDTO, MultipartFile file) throws IOException {
        boardDTO.setLikeCount(0);
        boardDTO.setViewCount(0);
        boardDTO.setCommentCount(0);

        String newFileName = "";
        if (!file.isEmpty()) {
            newFileName = fileUpload.upload(file, imgUploadLocation);
        }
        boardDTO.setBoardImg(newFileName);

        BoardEntity boardEntity = modelMapper.map(boardDTO, BoardEntity.class);
        boardRepository.save(boardEntity);
    }

    // 수정
    public void update(BoardDTO boardDTO, MultipartFile file) throws IOException {

        BoardEntity boardEntity = boardRepository.findById(boardDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("No board found with id: " + boardDTO.getId()));

        boardDTO.setLikeCount(boardEntity.getLikeCount());
        boardDTO.setViewCount(boardEntity.getViewCount());
        boardDTO.setCommentCount(boardEntity.getCommentCount());

        String deleteFile = boardEntity.getBoardImg();
        String newFileName = "";

        if (file != null && !file.isEmpty()) {
            if (deleteFile != null && !deleteFile.isEmpty()) {
                fileUpload.deleteFile(deleteFile, imgUploadLocation);
            }
            newFileName = fileUpload.upload(file, imgUploadLocation);
            boardDTO.setBoardImg(newFileName);
        }else {
            // 파일이 없으면 기존 이미지 유지
            boardDTO.setBoardImg(boardEntity.getBoardImg());
        }

        modelMapper.map(boardDTO, boardEntity);
        boardRepository.save(boardEntity);
    }

    // 삭제
    public void delete(Integer id) throws IOException {
        BoardEntity boardEntity = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No board found with id: " + id));

        fileUpload.deleteFile(boardEntity.getBoardImg(), imgUploadLocation);
        boardRepository.deleteById(id);
    }

    // 개별 조회
    public BoardDTO findById(Integer id, String pandan) {
        if (pandan.equals("R")) {
            boardRepository.viewcnt(id);
        }

        BoardEntity boardEntity = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No board found with id: " + id));

        return modelMapper.map(boardEntity, BoardDTO.class);

    }
    //공지사항만 조회
    public List<BoardDTO> getNoticeBoards(String category) {
        CategoryType categoryType = CategoryType.valueOf(category.toUpperCase());
        List<BoardEntity> noticeEntities = boardRepository.findByCategory(categoryType, Sort.by(Sort.Direction.DESC, "regDate"));
        return noticeEntities.stream()
                .map(data -> modelMapper.map(data, BoardDTO.class))
                .collect(Collectors.toList());
    }

    // 공지사항 제외 조회
    public Page<BoardDTO> list(String searchType, String searchKeyword, String category, Pageable pageable) {
        CategoryType categoryType = category.isEmpty() ? null : CategoryType.valueOf(category.toUpperCase());

        // Pageable 객체를 regDate 기준으로 내림차순 정렬하도록 설정
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "regDate"));

        Page<BoardEntity> read;

        if (categoryType == CategoryType.공지사항) {
            // 공지사항 카테고리일 경우 공지사항만 가져오기
            read = boardRepository.findByCategory(CategoryType.공지사항, sortedPageable);
        } else {
            // 일반 게시글 처리
            if (searchType.equals("title")) {
                read = (categoryType == null)
                        ? boardRepository.findByTitleContainingAndCategoryNot(searchKeyword, CategoryType.공지사항, sortedPageable)
                        : boardRepository.findByTitleContainingAndCategory(searchKeyword, categoryType, sortedPageable);
            } else if (searchType.equals("content")) {
                read = (categoryType == null)
                        ? boardRepository.findByContentContainingAndCategoryNot(searchKeyword, CategoryType.공지사항, sortedPageable)
                        : boardRepository.findByContentContainingAndCategory(searchKeyword, categoryType, sortedPageable);
            } else if (searchType.equals("titleContent")) {
                read = (categoryType == null)
                        ? boardRepository.findByTitleContainingOrContentContainingAndCategoryNot(searchKeyword, searchKeyword, CategoryType.공지사항, sortedPageable)
                        : boardRepository.findByTitleContainingOrContentContainingAndCategory(searchKeyword, searchKeyword, categoryType, sortedPageable);
            } else {
                read = (categoryType == null)
                        ? boardRepository.findByCategoryNot(CategoryType.공지사항, sortedPageable)
                        : boardRepository.findByCategory(categoryType, sortedPageable);
            }
            return read.map(data -> modelMapper.map(data, BoardDTO.class));
        }

        if (pageable.getPageNumber() >= 1) {
            List<BoardEntity> filteredContent = read.getContent().stream()
                    .filter(board -> !board.getCategory().equals(CategoryType.공지사항))
                    .collect(Collectors.toList());
            Page<BoardEntity> filteredPage = new PageImpl<>(filteredContent, pageable, read.getTotalElements());
            return filteredPage.map(data -> modelMapper.map(data, BoardDTO.class));
        } else {
            return read.map(data -> modelMapper.map(data, BoardDTO.class));
        }
    }

    public Page<BoardEntity> findByCategory(CategoryType category, Pageable pageable) {
            return boardRepository.findByCategory(category, pageable);
        }

    // 추천 수 증가
    @Transactional
    public void increaseLikeCount(Integer boardId, String username) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("No board found with id: " + boardId));
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("No user found with username: " + username));

        // 이미 추천했는지 여부 확인
        if (likeRepository.findByBoardAndUser(board, user).isPresent()) {
            throw new IllegalStateException("Already liked the post.");
        }

        // 추천 추가
        LikeEntity like = new LikeEntity();
        like.setBoard(board);
        like.setUser(user);
        likeRepository.save(like);

        boardRepository.increaseLikeCount(boardId);
    }

    // 추천 수 감소
    @Transactional
    public void decreaseLikeCount(Integer boardId, String username) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("No board found with id: " + boardId));
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("No user found with username: " + username));

        LikeEntity like = likeRepository.findByBoardAndUser(board, user)
                .orElseThrow(() -> new IllegalStateException("Not liked the post."));

        likeRepository.delete(like);
        boardRepository.decreaseLikeCount(boardId);
    }

    public boolean hasLiked(Integer boardId, String username) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("No board found with id: " + boardId));
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("No user found with username: " + username));

        return likeRepository.findByBoardAndUser(board, user).isPresent();
    }

    public Page<BoardDTO> findByAuthor(String author, Pageable pageable) {
        return boardRepository.findByAuthor(pageable, author).map(entity -> modelMapper.map(entity, BoardDTO.class));
    }
    //내 게시글
    public Page<BoardDTO> myBoards(String username, Pageable pageable) {
        Page<BoardEntity> boardEntities = boardRepository.findByAuthor(pageable, username);
        return boardEntities.map(entity -> modelMapper.map(entity, BoardDTO.class));
    }
}
