package com.woori.studylogin.Service;

import com.woori.studylogin.DTO.CommentDTO;
import com.woori.studylogin.Entity.BoardEntity;
import com.woori.studylogin.Entity.CommentEntity;
import com.woori.studylogin.Entity.UserEntity;
import com.woori.studylogin.Repository.BoardRepository;
import com.woori.studylogin.Repository.CommentRepository;
import com.woori.studylogin.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;


    public void register(Integer no, CommentDTO commentDTO,String username) {
        Optional<BoardEntity> data = boardRepository.findById(no);
        Optional<UserEntity> userData = userRepository.findByUsername(username); // 추가된 부분

        BoardEntity boardEntity = data.orElseThrow();
        UserEntity userEntity = userData.orElseThrow();

        CommentEntity commentEntity = modelMapper.map(commentDTO, CommentEntity.class);
        commentEntity.setBoard(boardEntity);
        commentEntity.setUser(userEntity);
        commentRepository.save(commentEntity);

        boardEntity.setCommentCount(boardEntity.getCommentCount() + 1);
        boardRepository.save(boardEntity);
    }

    public void remove(Integer id) {
        Optional<CommentEntity> commentEntityOpt = commentRepository.findById(id);
        if (commentEntityOpt.isPresent()) {
            CommentEntity commentEntity = commentEntityOpt.get();
            BoardEntity boardEntity = commentEntity.getBoard();

            commentRepository.deleteById(id);

            // 댓글 수 업데이트
            boardEntity.setCommentCount(boardEntity.getCommentCount() - 1);
            boardRepository.save(boardEntity);
        }
    }

    public CommentDTO read(Integer id) {
        Optional<CommentEntity> commentEntity = commentRepository.findById(id);
        CommentDTO commentDTO = modelMapper.map(commentEntity, CommentDTO.class);
        return commentDTO;

    }

//    public List<CommentDTO> list(Integer boardId) {
//        List<CommentEntity> commentEntities = commentRepository.findByComment(boardId);
//        List<CommentDTO> commentDTOS = Arrays.asList(modelMapper.map(
//                commentEntities, CommentDTO[].class
//        ));
//        return commentDTOS;
//    }


    public Page<CommentDTO> getCommentsByPage(Integer boardId, Pageable pageable) {
        Page<CommentEntity> commentEntities = commentRepository.findByComment(boardId, pageable);
        return commentEntities.map(commentEntity -> modelMapper.map(commentEntity, CommentDTO.class));
    }

    public Page<CommentDTO> myComments(String username, Pageable pageable) {
        Page<CommentEntity> commentEntities = commentRepository.findByUser(username, pageable);
        return commentEntities.map(commentEntity -> modelMapper.map(commentEntity, CommentDTO.class));
    }
}
