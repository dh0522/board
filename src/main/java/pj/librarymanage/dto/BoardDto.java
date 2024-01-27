package pj.librarymanage.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import pj.librarymanage.entity.BoardEntity;
import pj.librarymanage.entity.BoardFileEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

// DTO (Data Transfer Object) : 데이터 전송할 때 쓰는 객체
@Getter
@Setter
@ToString
// 필드 값 확인할 때 쓴다.
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자
public class BoardDto {

    private Long id;
    private String boardWriter;
    private String boardPass;
    private String boardTitle;
    private String boardContents;

    private int boardHits;

    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdatedTime;

    //save.html -> Controller로 넘어올 때 파일 담는 용도
    // 파일이 여러개 - > List로 받기
    private List<MultipartFile> boardFile;

    // 원본 파일 이름
    private List<String> originalFileName;

    // 서버 저장용 파일 이름 : 혹시나 있을 파일이름의 중복을 없애기 위해서.
    private List<String> storedFileName;

    // 파일 첨부 여부 ( 첨부 1 , 미첨부 0 )
    private int fileAttached;

    public BoardDto(Long id, String boardWriter, String boardTitle, int boardHits, LocalDateTime boardCreatedTime) {

        this.id = id;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardHits = boardHits;
        this.boardCreatedTime = boardCreatedTime;

    }

    public static BoardDto toBoardDto(BoardEntity boardEntity){

        BoardDto boardDto = new BoardDto();

        boardDto.setId(boardEntity.getId());
        boardDto.setBoardWriter(boardEntity.getBoardWriter());
        boardDto.setBoardPass(boardEntity.getBoardPass());
        boardDto.setBoardTitle(boardEntity.getBoardTitle());
        boardDto.setBoardContents(boardEntity.getBoardContents());
        boardDto.setBoardHits(boardEntity.getBoardHits());
        boardDto.setBoardCreatedTime(boardEntity.getCreatedTime());
        boardDto.setBoardUpdatedTime(boardEntity.getUpdatedTime());

        if ( boardEntity.getFileAttached() == 0 ){
            // 파일 없을 때
            boardDto.setFileAttached(boardEntity.getFileAttached());
        }else{
            //파일 있을 때
            List<String> originalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();

            for(BoardFileEntity boardFileEntity : boardEntity.getBoardFileEntityList()){

                originalFileNameList.add(boardFileEntity.getOriginalFileName());
                storedFileNameList.add(boardFileEntity.getStoredFileName());
            }
            boardDto.setOriginalFileName(originalFileNameList);
            boardDto.setStoredFileName(storedFileNameList);

/*
            boardDto.setFileAttached(boardEntity.getFileAttached());

            // 파일 이름을 가져가야 한다.
            // 그래야 저장경로에 있는 파일을 화면에다가 보여줄 수가 있다.
            // originalFileName , storedFileName : board_file_table(BoardFileEntity)에 들어있다.
            // 근데 우리는 지금 boardentity만 가지고 왔음
            // join
            // select * from board_table b, board_file_table bf where b.id=bf.board_id
            // and where b.id=?
            // 부모 엔티티 객체가 자식 엔티티 객체에 접근 가능
            boardDto.setOriginalFileName(boardEntity.getBoardFileEntityList().get(0).getOriginalFileName());
            boardDto.setStoredFileName(boardEntity.getBoardFileEntityList().get(0).getStoredFileName());
*/

        }

        return boardDto;
    }

}
