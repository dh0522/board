package pj.librarymanage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Id;

@Entity
@Getter
@Setter
@Table(name = "board_file_table")
public class BoardFileEntity extends BaseEntity {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String originalFileName;

    @Column
    private String storedFileName;

    // boardfileentity 기준으로 board와 어떤 관계인지!
    // board 1개 : boardfile 여러개 -> boardfile 입장에서 다대일 관계이다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id") // table(실제 db) 에 만들어지는 컬럼이름
    private BoardEntity boardEntity; // 부모 엔티티타입으로 정의해줘야한다.

    public static BoardFileEntity toBoardFileEntity(BoardEntity boardEntity ,String originalFileName , String storedFileName) {

        BoardFileEntity boardFileEntity = new BoardFileEntity();
        boardFileEntity.setOriginalFileName(originalFileName);
        boardFileEntity.setStoredFileName(storedFileName);
        boardFileEntity.setBoardEntity(boardEntity);
        return boardFileEntity;
    }
}
