package pj.librarymanage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pj.librarymanage.dto.BoardDto;
import pj.librarymanage.dto.CommentDto;
import pj.librarymanage.service.BoardService;
import pj.librarymanage.service.CommentService;

import javax.xml.stream.events.Comment;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
// board로 시작해서 그 이하의 주소를 getmapping에서 써주면 된다.
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping("/save")
    public String saveForm(){
        return "save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardDto boardDto ) throws IOException {
        System.out.println("boardDto = " + boardDto);
        boardService.save(boardDto);
        return "index";
    }

    @GetMapping("/")
    public String findAll(Model model) {
        // 전체 목록을 db에서 가져올 땐 model객체 사용한다.
        // DB에서 전체 게시글 데이터를 가져와서 list.html에 보여준다.
        List<BoardDto> boardDtoList = boardService.findAll();
        model.addAttribute("boardList",boardDtoList);
        return "list";

    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id , Model model,
                           @PageableDefault(page=1) Pageable pageable){
        // 경로 상에 있는 데이터를 가져올 때 pathvariable 사용
        /*
            해당 게시글의 조회수를 하나 올리고
            게시글 데이터를 가져와서 detail.html에 출력
         */
        boardService.updateHits(id);
        BoardDto boardDto = boardService.findById(id);

        /*
            댓글 목록 가져오기
         */
        List<CommentDto> commentDtoList = commentService.findAll(id);
        model.addAttribute("commentList",commentDtoList);
        //
        model.addAttribute("board" , boardDto);
        model.addAttribute("page", pageable.getPageNumber());
        return "detail";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id , Model model ){
        BoardDto boardDto = boardService.findById(id);
        model.addAttribute("boardUpdate", boardDto);
        return "update";
    }

    @PostMapping("/update")
    public String update( @ModelAttribute BoardDto boardDto , Model model ){

        BoardDto board = boardService.update(boardDto);
        model.addAttribute("board" , board);
        return "detail";
        //return "redirect:/board/" + boardDto.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id ){
        boardService.delete(id);
        return "redirect:/board/";
    }

    // /board/paging?page=1
    @GetMapping("/paging")
    public String paging(@PageableDefault(page = 1) Pageable pageable , Model model ){
       // pageable.getPageNumber();
        Page<BoardDto> boardList = boardService.paging(pageable);

        // page 갯수 = 20 개
        // 현재 사용자가 3페이지
        // 1 2 3 4 5
        // -> 3 이 색이 다르고, 다른 숫자들은 링크가 연결되어 있음.
        // 보여지는 페이지 갯수 3개
        // 현재 사용자가 7페이지에 있다면 -> 7 8 9 만 보이게

        // 보여지는 페이지 갯수
        int blockLimit = 3;

        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;// 1 4 7 11 ~
        int endPage = ((startPage + blockLimit -1 ) < boardList.getTotalPages()) ? startPage + blockLimit -1 : boardList.getTotalPages(); // 3 6 9  ~

        model.addAttribute("boardList",boardList);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);

        return "paging";

    }
}













