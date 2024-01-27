package pj.librarymanage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pj.librarymanage.dto.BoardDto;
import pj.librarymanage.entity.BoardEntity;
import pj.librarymanage.entity.BoardFileEntity;
import pj.librarymanage.repository.BoardFileRepository;
import pj.librarymanage.repository.BoardRepository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

// DTO -> Entity (Entity Class) : controller 넘겨 받을땐 dto로 받고 넘겨줄땐 entity로 줌
// Entity -> DTO (DTO Class) : db 조회 할 땐 repository로부터 entity로 넘겨받고 controller로 줄땐 dto로 줌
// 로 변환하는 작업들이 Service에서 많이 생김 (JPA특성)



@Service
@RequiredArgsConstructor
public class BoardService {


    private final BoardRepository boardRepository; // 생성자 주입 방식
    private final BoardFileRepository boardFileRepository;

    public void save(BoardDto boardDto) throws IOException {
        // 파일 첨부 여부에 따라 로직을 분리해야 한다.
        if( boardDto.getBoardFile().isEmpty() ){
            // 첨부 파일이 없을 때

            BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDto);
            boardRepository.save(boardEntity);

        }else{
            // 첨부 파일 있을 때
            /*
                1.DTO 에 담긴 파일을 꺼냄
                2.파일의 이름 가져옴
                3.서버 저장용 이름을 만듦
                // 내사진.jpg => 838128312381_내사진.jpg
                4.저장 경로 설정
                5.해당 경로에 파일 저장
                6.board_table에 해당 데이터 save 처리
                7.board_file_table에 해당 데이터 save 처리
             */

            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDto);
            Long savedId = boardRepository.save(boardEntity).getId();

            // 부모 entity를 db로부터 다시 가져옴
            BoardEntity board = boardRepository.findById(savedId).get();

            for( MultipartFile boardFile : boardDto.getBoardFile()){

                // MultipartFile boardFile = boardDto.getBoardFile(); // 1 : 파일을 꺼내는 것.
                String originalFilename = boardFile.getOriginalFilename(); // 2
                // 절대로 겹치지 않을 수 -> currnetTimeMillis() 이용
                String storedFileName = System.currentTimeMillis() + "_" + originalFilename; // 3
                //저장 경로 : "springboot_img"폴더가 실제로 존재해야함.
                String savePath = "/Users/dohyunhong/springboot_img/" + storedFileName; // 4
                boardFile.transferTo(new File(savePath)); // 5

                BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName);
                boardFileRepository.save(boardFileEntity);
                // db 에 저장까지 함.

            }



        }


    }

    @Transactional
    public List<BoardDto> findAll() {
        // repository에서 무언가를 가져올 때는 대부분 entity로 온다.
        // list형태의 entity로 넘어온다.

        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDto> boardDtoList = new ArrayList<>();

        // entity 객체를 Dto객체로 옮겨 담는다.
        // dto 객체에 생성

        for( BoardEntity boardEntity : boardEntityList){
            boardDtoList.add(BoardDto.toBoardDto(boardEntity));
        }

        return boardDtoList;
    }

    @Transactional
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }


    @Transactional
    public BoardDto findById( Long id ){
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);

        if( optionalBoardEntity.isPresent() ){
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDto boardDto = BoardDto.toBoardDto(boardEntity);
            return boardDto;
        }else
            return null;
    }

    public BoardDto update(BoardDto boardDto) {
        // jpa 는 save method = update 와 Insert 두가지 가능하다.
        // 어떻게 아느냐 ? -> id값의 유무!
        // insert = > id가 고려되지 않음
        // update => id가 필요함
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDto);
        boardRepository.save(boardEntity);

        return findById(boardDto.getId());
    }

    public void delete(Long id) {

        boardRepository.deleteById(id);

    }

    public Page<BoardDto> paging(Pageable pageable) {

        // 몇 페이지를 보고 싶은지
        int page = pageable.getPageNumber() - 1;

        // 한 페이지에 보여줄 글 갯수
        int pageLimit = 3;

        // 한 페이지당 3개씩 글을 보여주고 정렬 기준은 id(entity id) 기준으로 내림차순 정렬
        // page 위치에 있는 값은  0부터 시작 ( 보여지는건 1이지만 , 실제위치는 0 이다. )
        Page<BoardEntity> boardEntities =
                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글 갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부 true false
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부 true false
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부 true false

        // entity 객체 -> Dto 객체로 옮겨 담음
        // 목록 : id , writer , title , hits , createdTime
        Page<BoardDto> boardDtos = boardEntities.map(board -> new BoardDto(board.getId(), board.getBoardWriter(),
                board.getBoardTitle(), board.getBoardHits(), board.getCreatedTime()));

        return boardDtos;
    }
}
