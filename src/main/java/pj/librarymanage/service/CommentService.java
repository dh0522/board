package pj.librarymanage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pj.librarymanage.dto.CommentDto;
import pj.librarymanage.entity.BoardEntity;
import pj.librarymanage.entity.CommentEntity;
import pj.librarymanage.repository.BoardRepository;
import pj.librarymanage.repository.CommentRepository;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public Long save(CommentDto commentDto) {
        /*
            부모엔티티 (BoardEntity) 조회
         */
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(commentDto.getBoardId());
        if(optionalBoardEntity.isPresent()) {

            BoardEntity boardEntity = optionalBoardEntity.get();
            CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDto, boardEntity);
            return commentRepository.save(commentEntity).getId();
        }else{
            return null;
        }



    }

    public List<CommentDto> findAll(Long boardId) {
        // select * from comment_table where board_id=? order by id desc;
        BoardEntity boardEntity = boardRepository.findById(boardId).get();
        List<CommentEntity> commentEntityList = commentRepository.findAllByBoardEntityOrderByIdDesc(boardEntity);
        /*
            EntityList -> DtoList
         */

        List<CommentDto> commentDtoList = new ArrayList<>();
        for( CommentEntity commentEntity : commentEntityList ){
            CommentDto commentDto = CommentDto.toCommentDto(commentEntity, boardId);
            commentDtoList.add(commentDto);
        }
        return commentDtoList;
    }
}
