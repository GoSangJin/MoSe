package com.woori.studylogin.Service;

import com.woori.studylogin.DTO.CommentDTO;
import com.woori.studylogin.Entity.BoardEntity;
import com.woori.studylogin.Entity.CommentEntity;
import com.woori.studylogin.Repository.BoardRepository;
import com.woori.studylogin.Repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;

    public void register(Integer no, CommentDTO commentDTO) {
        Optional<BoardEntity> data = boardRepository.findById(no);
        BoardEntity boardEntity = data.orElseThrow();
        CommentEntity commentEntity = modelMapper.map(commentDTO, CommentEntity.class);
        commentEntity.setBoardEntity(boardEntity);
        commentRepository.save(commentEntity);
    }

    public void remove(Integer id) {
        commentRepository.deleteById(id);
    }

    public CommentDTO read(Integer id) {
        Optional<CommentEntity> commentEntity = commentRepository.findById(id);
        CommentDTO commentDTO = modelMapper.map(commentEntity, CommentDTO.class);
        return commentDTO;
    }

    public List<CommentDTO> list(Integer boardId) {
        List<CommentEntity> commentEntities = commentRepository.findByComment(boardId);
        List<CommentDTO> commentDTOS = Arrays.asList(modelMapper.map(
                commentEntities, CommentDTO[].class
        ));
        return commentDTOS;
    }
}
