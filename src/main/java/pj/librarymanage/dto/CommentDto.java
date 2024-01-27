package pj.librarymanage.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pj.librarymanage.entity.CommentEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentDto {
    private Long id;
    private String commentWriter;
    private String commentContents;
    private Long boardId;
    private LocalDateTime commentCreatedTime;

    public static CommentDto toCommentDto(CommentEntity commentEntity , Long boardId) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(commentEntity.getId());
        commentDto.setCommentWriter(commentEntity.getCommentWriter());
        commentDto.setCommentContents(commentEntity.getCommentContents());
        commentDto.setCommentCreatedTime(commentEntity.getCreatedTime());
//        commentDto.setBoardId(commentEntity.getBoardEntity().getId()); <= 이렇게 쓰려면 Service 메서드에 @Transactional 붙이기
        commentDto.setBoardId(boardId);
        return commentDto;

    }
}
