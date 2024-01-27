package pj.librarymanage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pj.librarymanage.dto.CommentDto;

import javax.xml.stream.events.Comment;

@Entity
@Getter
@Setter
@Table(name = "comment_table")
public class CommentEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String commentWriter;

    @Column
    private String commentContents;

    /*
        Board : Comment = 1 : N
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private BoardEntity boardEntity;

    public static CommentEntity toSaveEntity(CommentDto commentDto , BoardEntity boardEntity) {

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setCommentWriter(commentDto.getCommentWriter());
        commentEntity.setCommentContents(commentDto.getCommentContents());
        commentEntity.setBoardEntity(boardEntity);

        return commentEntity;

    }
}
